package com.example.helperjc.data.network

data class AiResponse(
    val choices: List<Choice>
) {
    data class Choice(
        val message: Message
    )
    data class Message(
        val content: String
    )
}