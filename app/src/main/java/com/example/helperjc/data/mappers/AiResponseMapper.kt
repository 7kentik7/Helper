package com.example.helperjc.data.network.mappers

import com.example.helperjc.data.network.PlanDto
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import javax.inject.Inject

data class AiErrorDto(val error: String? = null)

class AiResponseMapper @Inject constructor() {

    private val gson = Gson()


    fun extractJson(text: String): String? {
        val cleaned = text
            .replace(Regex("```json\\s*"), "")
            .replace(Regex("```\\s*"), "")
            .trim()

        val start = cleaned.indexOf('{')
        val end = cleaned.lastIndexOf('}')

        if (start == -1 || end == -1 || start >= end) return null

        return cleaned.substring(start, end + 1)
    }

    fun parseErrorOrNull(jsonText: String): String? {
        return try {
            val errorDto = gson.fromJson(jsonText, AiErrorDto::class.java)
            if (!errorDto.error.isNullOrBlank()) errorDto.error else null
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    fun parsePlan(jsonText: String): PlanDto? {
        return try {
            val dto = gson.fromJson(jsonText, PlanDto::class.java)
            if (dto.title.isBlank()) null else dto
        } catch (e: JsonSyntaxException) {
            null
        }
    }
}