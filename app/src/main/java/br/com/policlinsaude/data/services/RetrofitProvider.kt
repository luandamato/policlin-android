package br.com.policlinsaude.data.services

import android.util.Log
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
 * Interceptor de log de rede que imprime TODOS os dados do request
 * (método, URL, headers, body) e do response (status, headers, body).
 */
class LoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val method = request.method

        val requestBodyText = request.body.copyBody() ?: "sem body"

        Log.d(TAG, "========== REQUEST ($method) ==========")
        Log.d(TAG, "URL: ${request.url}")
        request.headers.forEach { header ->
            Log.d(TAG, "Header: ${header.first} = ${header.second}")
        }
        Log.d(TAG, "Body: $requestBodyText")

        val startedAt = System.currentTimeMillis()

        return try {
            val response = chain.proceed(request)
            val elapsed = System.currentTimeMillis() - startedAt
            val responseBodyText = response.peekBody(1_000_000L)?.string() ?: "sem body"

            Log.d(TAG, "========== RESPONSE ==========")
            Log.d(TAG, "Code: ${response.code}")
            Log.d(TAG, "Message: ${response.message}")
            Log.d(TAG, "Duration: ${elapsed}ms")
            response.headers.forEach { header ->
                Log.d(TAG, "Header: ${header.first} = ${header.second}")
            }
            Log.d(TAG, "Body: $responseBodyText")

            response
        } catch (e: Exception) {
            Log.d(TAG, "ERROR ao chamar API: ${e.message}", e)
            throw e
        }
    }

    private companion object {
        const val TAG = "RETROFIT"
    }
}

private fun RequestBody?.copyBody(): String? {
    if (this == null) return null
    val buffer = okio.Buffer()
    writeTo(buffer)
    return buffer.readUtf8()
}