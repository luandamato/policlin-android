package br.com.policlinsaude.data.services

import androidx.multidex.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    private const val VERSION_ANDROID = BuildConfig.VERSION_NAME

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
                .header(VERSION, VERSION_ANDROID)
            chain.proceed(requestBuilder.build())
        }

        clientBuilder.addInterceptor(
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            }
        )

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(providesGson()))
            .build()
            .create(service)
    }
}