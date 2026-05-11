package com.example.helperjc.domain.plans.usecases

import androidx.compose.ui.graphics.Color
import com.example.helperjc.BuildConfig
import com.example.helperjc.data.local.database.PlanDao
import com.example.helperjc.data.local.database.TaskDao
import com.example.helperjc.data.local.database.models.PlanDbModel
import com.example.helperjc.data.local.database.models.TaskDbModel
import com.example.helperjc.data.network.AiApiService
import com.example.helperjc.data.network.AiRequest
import com.example.helperjc.data.network.PlanDto
import com.example.helperjc.enums.PlanRepeatType
import com.example.helperjc.enums.TaskPriority
import com.example.helperjc.utils.toHex
import com.example.helperjc.utils.toTimeInMillis
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.time.LocalDateTime
import javax.inject.Inject

class GeneratePlanUseCase @Inject constructor(
    private val aiApiService: AiApiService,
    private val planDao: PlanDao,
    private val taskDao: TaskDao,
) {
    private val systemPrompt = """
    Ты помощник по планированию. Пользователь описывает свою цель.
    Создай план с конкретными задачами. Количество задач определяй сам исходя из цели.
    Отвечай ТОЛЬКО валидным JSON без пояснений и markdown-блоков:
    {
      "title": "Название плана",
      "tasks": [
        {"title": "...", "description": null, "priority": "HIGH"},
        {"title": "...", "description": null, "priority": "MEDIUM"}
      ]
    }
    Требования к задачам:
    - title: короткое название, максимум 4-5 слов
    - description: всегда null
    - priority: только LOW, MEDIUM, HIGH
""".trimIndent()

    suspend operator fun invoke(userGoal: String): Result<Unit> = runCatching {

        val response = aiApiService.generateContent(
            token = "Bearer ${BuildConfig.GEMINI_API_KEY}",
            request = AiRequest(
                model = "gemini-2.5-flash-preview-04-17",
                messages = listOf(
                    AiRequest.Message(role = "system", content = systemPrompt),
                    AiRequest.Message(role = "user", content = userGoal)
                )
            )
        )

        val content = response.choices
            .firstOrNull()?.message?.content
            ?: throw IllegalStateException("Пустой ответ от API")

        val jsonText = content.extractJson()

        val dto = try {
            Gson().fromJson(jsonText, PlanDto::class.java)
        } catch (e: JsonSyntaxException) {
            throw IllegalStateException("Не удалось разобрать ответ от ИИ: ${e.message}")
        }

        if (dto.title.isBlank()) {
            throw IllegalStateException("ИИ вернул план без названия")
        }

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
            val priority = try {
                TaskPriority.valueOf(task.priority.uppercase())
            } catch (e: IllegalArgumentException) {
                TaskPriority.MEDIUM
            }

            taskDao.addEditTask(
                TaskDbModel(
                    id = 0,
                    title = task.title,
                    description = task.description,
                    isActive = false,
                    priority = priority.name,
                    planId = planId.toInt()
                )
            )
        }
    }

    private fun String.extractJson(): String {
        val cleaned = this
            .replace(Regex("```json\\s*"), "")
            .replace(Regex("```\\s*"), "")
            .trim()

        val start = cleaned.indexOf('{')
        val end = cleaned.lastIndexOf('}')

        if (start == -1 || end == -1 || start >= end) {
            throw IllegalStateException("JSON не найден в ответе API")
        }

        return cleaned.substring(start, end + 1)
    }
}