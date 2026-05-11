package com.example.helperjc.domain.plans.usecases
import com.example.helperjc.BuildConfig
import com.example.helperjc.data.local.database.PlanDao
import com.example.helperjc.data.local.database.TaskDao
import com.example.helperjc.data.mappers.PlanDtoMapper
import com.example.helperjc.data.network.AiApiService
import com.example.helperjc.data.network.AiRequest
import com.example.helperjc.data.network.AiResponse
import com.example.helperjc.data.network.AsyncResult
import com.example.helperjc.data.network.PlanDto
import com.example.helperjc.data.network.mappers.AiResponseMapper
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
class GeneratePlanUseCase @Inject constructor(
    private val aiApiService: AiApiService,
    private val planDao: PlanDao,
    private val taskDao: TaskDao,
    private val aiResponseMapper: AiResponseMapper,
    private val planDtoMapper: PlanDtoMapper
) {

    suspend operator fun invoke(userGoal: String): AsyncResult<Unit> {
        return try {
            val response = sendRequestToAi(userGoal)

            val responseText = extractTextFromResponse(response)
                ?: return AsyncResult.Error("Пустой ответ от AI")

            val jsonText = aiResponseMapper.extractJson(responseText)
                ?: return AsyncResult.Error("Не удалось найти JSON в ответе")

            val aiError = aiResponseMapper.parseErrorOrNull(jsonText)
            if (aiError != null) return AsyncResult.Error(aiError)

            val planDto = aiResponseMapper.parsePlan(jsonText)
                ?: return AsyncResult.Error("Не удалось разобрать план из ответа AI")

            savePlanToDatabase(planDto)

            AsyncResult.Success(Unit)

        } catch (e: HttpException) {
            AsyncResult.Error(mapHttpError(e.code()))
        } catch (e: IOException) {
            AsyncResult.Error("Нет подключения к интернету")
        } catch (e: Exception) {
            AsyncResult.Error(e.message ?: "Неизвестная ошибка")
        }
    }

    private suspend fun sendRequestToAi(userGoal: String): AiResponse {
        return aiApiService.generateContent(
            token = "Bearer ${BuildConfig.GEMINI_API_KEY}",
            request = AiRequest(
                messages = listOf(
                    AiRequest.Message(role = "system", content = SYSTEM_PROMPT),
                    AiRequest.Message(role = "user", content = userGoal)
                )
            )
        )
    }

    private fun extractTextFromResponse(response: AiResponse): String? {
        return response.choices.firstOrNull()?.message?.content
    }

    private suspend fun savePlanToDatabase(planDto: PlanDto) {
        val planDbModel = planDtoMapper.toPlanDbModel(planDto)
        val planId = planDao.addAiPlan(planDbModel)

        val taskDbModels = planDtoMapper.toTaskDbModels(planDto.tasks, planId.toInt())
        taskDbModels.forEach { task -> taskDao.addEditTask(task) }
    }

    private fun mapHttpError(code: Int): String = when (code) {
        400 -> "Неверный запрос к API"
        401 -> "Неверный API ключ"
        403 -> "Доступ запрещён"
        429 -> "Превышен лимит запросов, попробуйте позже"
        500, 502, 503 -> "Сервер недоступен, попробуйте позже"
        else -> "Ошибка сети: $code"
    }

    companion object {
        private val SYSTEM_PROMPT = """
            Ты помощник по планированию. Пользователь описывает свою цель.
            Если запрос НЕ связан с планированием, целями или задачами — верни ТОЛЬКО это:
            {"error": "Опишите цель или задачу, которую хотите достичь"}
            
            Если запрос связан с планированием — верни ТОЛЬКО валидный JSON:
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
    }
}