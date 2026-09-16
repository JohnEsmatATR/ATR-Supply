package com.akhnaton.atrapp.shared

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder

object RetrofitClient {


    fun <T> getInstance(service: Class<T>): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(ConstantLinks.BASE_URL)
            .client(SetupHttpClient().setupOkHttpClient())
            .addConverterFactory(
                GsonConverterFactory.create
                    (
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            )
            .build()

        return retrofit.create(service)
    }

    private fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()

        builder.apply {
            addInterceptor(ApiInterceptor())
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            addInterceptor(loggingInterceptor)
        }

        return builder.connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

            .build()
    }
}
