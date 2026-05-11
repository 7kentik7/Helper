package com.example.helperjc.data.network

data class AiRequest(
    val model: String = "gemini-2.5-flash-preview-04-17",
    val messages: List<Message>,
    val max_tokens: Int = 1000,
    val temperature: Double = 0.7
) {
    data class Message(
        val role: String,
        val content: String
    )
}