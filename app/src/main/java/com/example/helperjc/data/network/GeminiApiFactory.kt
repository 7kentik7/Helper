package com.example.helperjc.data.network

import com.example.helperjc.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GeminiApiFactory {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.GEMINI_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val geminiApiService: GeminiApiService = retrofit.create(GeminiApiService::class.java)
}