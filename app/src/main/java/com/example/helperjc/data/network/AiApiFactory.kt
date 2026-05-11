package com.example.helperjc.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object AiApiFactory {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://proxy.gen-api.ru/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
        )
        .build()

    val apiService: AiApiService = retrofit.create(AiApiService::class.java)
}
