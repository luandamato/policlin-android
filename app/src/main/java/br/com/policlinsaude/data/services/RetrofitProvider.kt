package br.com.policlinsaude.data.services

import android.util.Log
import br.com.policlinsaude.utils.LogManager
import br.com.policlinsaude.utils.LogTags
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Centralizador de instâncias Retrofit (padrão ServiceGenerator).
 *
 * Cria um Retrofit por serviço com os interceptors padrão do app
 * (plataforma/versão) e timeouts comuns.
 */
object RetrofitProvider {

    private const val PLATFORM = "plataforma"
    private const val PLATFORM_ANDROID = "A"
    private const val VERSION = "versao"

    fun providesGson(): Gson = GsonBuilder()
        .setDateFormat("dd-mm-yyyy hh:mm:ss")
        .create()

    fun <T> createService(
        service: Class<T>,
        baseUrl: String = NetworkConstants.BASE_URL,
        interceptors: List<Interceptor> = emptyList(),
        timeout: Long = 60L
    ): T {
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .callTimeout(timeout, TimeUnit.SECONDS)

        interceptors.forEach { clientBuilder.addInterceptor(it) }

        clientBuilder.addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
                .header(PLATFORM, PLATFORM_ANDROID)
                .header(VERSION, NetworkConstants.APP_VERSION)
            chain.proceed(requestBuilder.build())
        }

        clientBuilder.addInterceptor(LoggingInterceptor())

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(providesGson()))
            .build()
            .create(service)
    }
}

/**
 * Interceptor de log de rede que controla verbosidade via BuildConfig.DEBUG.
 *
 * Comportamento:
 * - DEBUG=true (development):  Logs detalhados (request/response completo)
 * - DEBUG=false (production):  Somente erros são registrados
 *
 * Para logs manuais em desenvolvimento, use LogManager.log()
 */
class LoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()

        // Em produção, apenas prosseguir sem logs
        if (!isDebug()) {
            return try {
                chain.proceed(request)
            } catch (e: Exception) {
                // Apenas erros são logados em produção
                LogManager.e(TAG, "ERRO NA API: ${request.method} ${request.url} - ${e.message}", e)
                throw e
            }
        }

        // Em desenvolvimento, logs detalhados
        val method = request.method
        val requestBodyText = request.body.copyBody() ?: "sem body"

        LogManager.d(TAG, "========== REQUEST ($method) ==========")
        LogManager.d(TAG, "URL: ${request.url}")
        request.headers.forEach { header ->
            LogManager.d(TAG, "Header: ${header.first} = ${header.second}")
        }
        LogManager.d(TAG, "Body: $requestBodyText")

        val startedAt = System.currentTimeMillis()

        return try {
            val response = chain.proceed(request)
            val elapsed = System.currentTimeMillis() - startedAt
            val responseBodyText = response.peekBody(1_000_000L)?.string() ?: "sem body"

            LogManager.d(TAG, "========== RESPONSE ==========")
            LogManager.d(TAG, "URL: ${request.url}")
            LogManager.d(TAG, "Code: ${response.code}")
            LogManager.d(TAG, "Message: ${response.message}")
            LogManager.d(TAG, "Duration: ${elapsed}ms")
            response.headers.forEach { header ->
                LogManager.d(TAG, "Header: ${header.first} = ${header.second}")
            }
            LogManager.d(TAG, "Body: $responseBodyText")

            response
        } catch (e: Exception) {
            LogManager.e(TAG, "ERRO ao chamar API (${method} ${request.url}): ${e.message}", e)
            throw e
        }
    }

    private companion object {
        private const val TAG = "DebugPoliclin"

        /**
         * Verifica se está em modo debug usando reflexão
         */
        private fun isDebug(): Boolean {
            return try {
                val buildConfigClass = Class.forName("br.com.policlinsaude.BuildConfig")
                val debugField = buildConfigClass.getField("DEBUG")
                debugField.getBoolean(null)
            } catch (e: Exception) {
                true // Fallback: assume DEBUG em caso de erro
            }
        }
    }
}

private fun RequestBody?.copyBody(): String? {
    if (this == null) return null
    val buffer = okio.Buffer()
    writeTo(buffer)
    return buffer.readUtf8()
}