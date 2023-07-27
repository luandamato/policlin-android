package br.com.data.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import com.google.gson.GsonBuilder
import okhttp3.Response
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkingModule {

    companion object {

        private const val API_URL : String = "http://policlinsaude.com.br/apiapp/"
        private const val TEST_URL : String = "http://policlinsaude.com.br/mapp/api/"
        private const val PLATFORM = "plataforma"
        private const val PLATFORM_ANDROID = "A"
        private const val VERSION = "versao"
        private const val VERSION_ANDROID = "1.41.0"

    }


    @Provides
    @Named("baseUrl")
    fun providesBaseUrl(): String = API_URL


    @Provides
    fun providesRxJava2CallAdapter(): RxJava2CallAdapterFactory
            = RxJava2CallAdapterFactory.create()

    @Provides
    fun providesGson(): Gson = GsonBuilder()
            .setDateFormat("dd-mm-yyyy hh:mm:ss")
            .create()

    @Provides
    fun providesGsonConverterFactory(gson: Gson): GsonConverterFactory
            = GsonConverterFactory.create(gson)

    @Provides
    fun providesOkHttpClient(logger: Interceptor): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                requestBuilder.header(PLATFORM, PLATFORM_ANDROID)
                requestBuilder.header(VERSION, VERSION_ANDROID)
                chain.proceed(requestBuilder.build())
            }
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS) // write timeout
            .readTimeout(60, TimeUnit.SECONDS) // read timeout
            .build()

    @Provides
    fun providesInterceptorLogger(): Interceptor {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        return logger
    }

    @Provides
    @Singleton
    fun providesRetrofit(rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
                         @Named("baseUrl") baseUrl: String, okHttpClient: OkHttpClient,
                         gsonConverterFactory: GsonConverterFactory): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .addConverterFactory(gsonConverterFactory)
            .baseUrl(baseUrl)
            .build()
}