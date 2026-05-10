package com.example.helperjc.data.network

data class GeminiResponse(
    val candidates: List<Candidate>
) {
    data class Candidate(
        val content: Content,
        val finishReason: String?
    )

    data class Content(
        val parts: List<Part>,
        val role: String = "model"
    )

    data class Part(val text: String)
}