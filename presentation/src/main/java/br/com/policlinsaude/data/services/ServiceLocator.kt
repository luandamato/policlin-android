package br.com.policlinsaude.data.services

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import br.com.policlinsaude.data.values.Constants
import java.util.concurrent.TimeUnit

/**
 * ServiceLocator / Localizador de Serviços
 * Substitui Hilt para injeção manual simples
 * Uso: ServiceLocator.apiService() ou ServiceLocator.preferences()
 */
object ServiceLocator {
    private var context: Context? = null
    private var retrofit: Retrofit? = null
    private var apiService: ApiService? = null
    private var preferences: SharedPreferences? = null
    private var gson: Gson? = null
    private var okHttpClient: OkHttpClient? = null

    fun init(appContext: Context) {
        context = appContext
    }

    fun apiService(): ApiService {
        if (apiService == null) {
            apiService = provideRetrofit().create(ApiService::class.java)
        }
        return apiService!!
    }

    fun preferences(): SharedPreferences {
        if (preferences == null) {
            preferences = context?.getSharedPreferences(
                Constants.PREF_NAME,
                Context.MODE_PRIVATE
            ) ?: throw IllegalStateException("Context not initialized in ServiceLocator")
        }
        return preferences!!
    }

    fun gson(): Gson {
        if (gson == null) {
            gson = Gson()
        }
        return gson!!
    }

    private fun provideRetrofit(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson()))
                .build()
        }
        return retrofit!!
    }

    private fun provideOkHttpClient(): OkHttpClient {
        if (okHttpClient == null) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            okHttpClient = OkHttpClient.Builder()
                .connectTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor { chain ->
                    // Adicionar Authorization header automaticamente
                    val originalRequest = chain.request()
                    val token = preferences().getString(Constants.PREF_TOKEN, null)
                    
                    val request = if (token != null) {
                        originalRequest.newBuilder()
                            .header(Constants.HEADER_AUTHORIZATION, "Bearer $token")
                            .build()
                    } else {
                        originalRequest
                    }
                    chain.proceed(request)
                }
                .build()
        }
        return okHttpClient!!
    }

    // Limpar cache quando fazer logout
    fun clear() {
        preferences().edit().clear().apply()
        apiService = null
    }
}
