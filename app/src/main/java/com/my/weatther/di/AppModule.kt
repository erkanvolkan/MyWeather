package com.my.weatther.di

import com.my.weatther.Utils
import com.my.weatther.service.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    // Provides a singleton instance of OkHttpClient with logging interceptor
    @Singleton
    @Provides
    fun getHttpClientInstance(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        val interceptor = httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    // Provides a singleton instance of AuthService (Retrofit) using the OkHttpClient
    @Singleton
    @Provides
    fun getRetrofitInstance(okHttpClient: OkHttpClient): AuthService {
        return Retrofit.Builder()
            .baseUrl(Utils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build().create(AuthService::class.java)
    }
}
