package com.example.helperjc.domain.ai.usecases

import androidx.compose.ui.graphics.Color
import com.example.helperjc.data.local.database.PlanDao
import com.example.helperjc.data.local.database.TaskDao
import com.example.helperjc.data.local.database.models.PlanDbModel
import com.example.helperjc.data.local.database.models.TaskDbModel

import com.example.helperjc.data.network.GeminiApiService
import com.example.helperjc.data.network.GeminiRequest
import com.example.helperjc.data.network.PlanDto

import com.example.helperjc.enums.PlanRepeatType
import com.example.helperjc.utils.toHex
import com.example.helperjc.utils.toTimeInMillis
import com.google.gson.Gson
import java.time.LocalDateTime
import javax.inject.Inject

class GeneratePlanUseCase @Inject constructor(
    private val geminiApiService: GeminiApiService,
    private val planDao: PlanDao,
    private val taskDao: TaskDao,
) {
    suspend operator fun invoke(userGoal: String): Result<Unit> = runCatching {
        val response = geminiApiService.generateContent(
            request = buildRequest(userGoal)
        )

        val jsonText = response.candidates.first().content.parts.first().text
        val dto = Gson().fromJson(jsonText, PlanDto::class.java)

        val planId = planDao.addAiPlan(
            PlanDbModel(
                id = 0,
                title = dto.title,
                color = Color.Gray.toHex(),
                repeatAt = PlanRepeatType.NONE.name,
                startTime = LocalDateTime.now().toTimeInMillis(),
                endTime = null
            )
        )

        dto.tasks.forEach { task ->
            taskDao.addEditTask(
                TaskDbModel(
                    id = 0,
                    title = task.title,
                    description = task.description,
                    isActive = false,
                    priority = task.priority,
                    planId = planId.toInt()
                )
            )
        }
    }

    private fun buildRequest(userGoal: String): GeminiRequest {
        val systemPrompt = """
            Ты помощник по планированию. Пользователь описывает свою цель.
            Создай план с конкретными задачами. Количество задач определяй сам исходя из цели.
            Отвечай ТОЛЬКО валидным JSON без пояснений и markdown:
            {
              "title": "Название плана",
              "tasks": [
                {"title": "...", "description": "...", "priority": "HIGH"},
                {"title": "...", "description": null, "priority": "MEDIUM"}
              ]
            }
            Для priority используй только: LOW, MEDIUM, HIGH
        """.trimIndent()

        return GeminiRequest(
            systemInstruction = GeminiRequest.SystemInstruction(
                parts = listOf(GeminiRequest.Part(systemPrompt))
            ),
            contents = listOf(
                GeminiRequest.Content(
                    parts = listOf(GeminiRequest.Part(userGoal))
                )
            )
        )
    }
}