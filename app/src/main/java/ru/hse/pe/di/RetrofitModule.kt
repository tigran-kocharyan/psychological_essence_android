package ru.hse.pe.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.hse.pe.BuildConfig
import ru.hse.pe.data.api.ApiService
import javax.inject.Singleton


/**
 * Module to DI the Retrofit
 */
@Module
class RetrofitModule {
    @Provides
    @Singleton
    fun provideApiService(builder: Retrofit.Builder, gson: Gson): ApiService = builder
        .baseUrl(SERVER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideRetrofitBuilder(client: OkHttpClient): Retrofit.Builder = Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())

    @Provides
    @Singleton
    fun provideOkHttp(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setDateFormat("yyyy-MM-dd").create()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)

    companion object {
        private const val SERVER_BASE_URL = "https://psessence.ru/"
    }
}