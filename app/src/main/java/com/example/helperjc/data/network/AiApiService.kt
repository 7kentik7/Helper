package com.example.helperjc.data.network

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AiApiService {
    @POST("v1/chat/completions")
    suspend fun generateContent(
        @Header("Authorization") token: String,
        @Body request: AiRequest
    ): AiResponse
}
