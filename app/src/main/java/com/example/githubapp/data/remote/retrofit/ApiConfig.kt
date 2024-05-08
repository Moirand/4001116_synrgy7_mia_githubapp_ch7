package com.example.githubapp.data.remote.retrofit

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    fun provideApiService(context: Context): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(provideOkhttpClient(context))
            .build()
            .create(ApiService::class.java)
    }

    private fun provideOkhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(provideHttpLoggingInterceptor())
            .addInterceptor(provideChuckerInterceptor(context))
            .addInterceptor(provideAuthInterceptor())
            .build()
    }

    private fun provideAuthInterceptor(): Interceptor {
        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .addHeader("Authorization", "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")
                .build()
            chain.proceed(requestHeaders)
        }
        return authInterceptor
    }

    private fun provideHttpLoggingInterceptor(): Interceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    private fun provideChuckerInterceptor(context: Context): Interceptor {
        return ChuckerInterceptor.Builder(context).build()
    }
}



