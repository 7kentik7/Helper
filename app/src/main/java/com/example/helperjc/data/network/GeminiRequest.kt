package com.example.helperjc.data.network


data class GeminiRequest(
    val contents: List<Content>,
    val systemInstruction: SystemInstruction? = null,
    val generationConfig: GenerationConfig = GenerationConfig()
) {
    data class Content(
        val role: String = "user",
        val parts: List<Part>
    )

    data class Part(
        val text: String
    )

    data class SystemInstruction(
        val parts: List<Part>
    )

    data class GenerationConfig(
        val responseMimeType: String = "application/json"
    )
}