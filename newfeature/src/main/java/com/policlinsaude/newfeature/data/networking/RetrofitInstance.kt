package com.policlinsaude.newfeature.data.networking

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL
import java.util.concurrent.TimeUnit

class RetrofitInstance (
    private val timeout: Long = 60L
) {

    companion object {
        const val API_NOTIFICATION = "http://policlinsaude.com.br/mapp2/api/"
        private const val API_URL : String = "http://policlinsaude.com.br/apiapp_prot/"
        private const val PLATFORM = "plataforma"
        private const val PLATFORM_ANDROID = "A"
        private const val VERSION = "versao"
        private const val VERSION_ANDROID = "1.41.0"
    }

    fun <T> create(service: Class<T>, baseUrl: String = API_URL, interceptors: List<Interceptor> = emptyList(), certificate: List<String> = emptyList()): T {

        val host = getHost(baseUrl)
        val client = getClient(host, interceptors, certificate)

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(providesGson()))
            .client(client)
            .build()
            .create(service)

    }

    private fun getHost(url: String): String {
        return URL(url).host
    }

    fun providesGson(): Gson = GsonBuilder()
        .setDateFormat("dd-mm-yyyy hh:mm:ss")
        .create()

    private fun getClient(host: String, interceptors: List<Interceptor>, certificate: List<String>): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        if(interceptors.isNotEmpty())
            clientBuilder.interceptors().addAll(interceptors)

        clientBuilder.addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.header(PLATFORM, PLATFORM_ANDROID)
            requestBuilder.header(VERSION, VERSION_ANDROID)
            chain.proceed(requestBuilder.build())
        }

        return clientBuilder
            .callTimeout(timeout, TimeUnit.SECONDS)
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }
}