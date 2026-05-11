package com.example.helperjc.domain.plans.usecases

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
import com.google.gson.JsonSyntaxException
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

        // Gemini 2.0+ может вернуть несколько parts (thinking + ответ).
        // Берём все текстовые parts и ищем тот, который содержит JSON.
        val parts = response.candidates.firstOrNull()?.content?.parts
            ?: throw IllegalStateException("Пустой ответ от API")

        val jsonText = parts
            .mapNotNull { it.text }
            .firstOrNull { it.contains("{") }
            ?.extractJson()
            ?: throw IllegalStateException("JSON не найден в ответе API")

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
            val safePriority = try {
                // Проверяем что priority валидный, иначе ставим MEDIUM
                com.example.helperjc.enums.TaskPriority.valueOf(task.priority.uppercase())
                task.priority.uppercase()
            } catch (e: IllegalArgumentException) {
                "MEDIUM"
            }

            taskDao.addEditTask(
                TaskDbModel(
                    id = 0,
                    title = task.title,
                    description = task.description,
                    isActive = false,
                    priority = safePriority,
                    planId = planId.toInt()
                )
            )
        }
    }

    private fun String.extractJson(): String {
        // Убираем markdown code block если есть
        val withoutMarkdown = this
            .replace(Regex("```json\\s*"), "")
            .replace(Regex("```\\s*"), "")
            .trim()

        // Ищем первую { и последнюю } — берём только JSON-объект
        val start = withoutMarkdown.indexOf('{')
        val end = withoutMarkdown.lastIndexOf('}')

        if (start == -1 || end == -1 || start >= end) {
            throw IllegalStateException("Не найден корректный JSON-объект в ответе")
        }

        return withoutMarkdown.substring(start, end + 1)
    }

    private fun buildRequest(userGoal: String): GeminiRequest {
        val systemPrompt = """
            Ты помощник по планированию. Пользователь описывает свою цель.
            Создай план с конкретными задачами. Количество задач определяй сам исходя из цели.
            Отвечай ТОЛЬКО валидным JSON без пояснений и markdown-блоков:
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
