package com.example.helperjc.notifications

import com.example.helperjc.enums.PlanRepeatType

object NotificationTextProvider {

    private val dailyMessages = listOf(
        "Ты справляешься! Ещё один день — ещё один шаг к цели 💪",
        "Не останавливайся, ты уже так много сделал! 🔥",
        "Каждый день приближает тебя к результату. Продолжай! ⚡",
        "Сегодня отличный день чтобы сделать чуть больше вчерашнего 🚀",
        "Ты на правильном пути. Держись! 🎯"
    )

    private val weeklyMessages = listOf(
        "Неделя прошла — как твой прогресс? Время проверить! 📊",
        "Ещё одна неделя позади. Ты становишься лучше с каждым днём! 🏆",
        "Еженедельная проверка: ты молодец, не сдавайся! 💡",
        "Посмотри как далеко ты зашёл за эту неделю 🌟",
        "Новая неделя — новые возможности. Вперёд! 🎉"
    )

    private val monthlyMessages = listOf(
        "Месяц позади — ты герой! Продолжай в том же духе 🦸",
        "Ещё один месяц ближе к своей мечте 🌙",
        "Месячный отчёт: ты справляешься лучше, чем думаешь! ✨",
        "Посмотри на свой путь — ты уже столького достиг! 🎖️",
        "Месяц прошёл. Ты не сдался — это уже победа! 🥇"
    )

    private val yearlyMessages = listOf(
        "Целый год упорного труда — ты невероятен! 🏅",
        "Год позади. Ты изменился и стал лучше 🌱",
        "Ещё один год на пути к своей цели. Так держать! 🔝",
        "Год упорства — ты заслуживаешь гордиться собой! 🎊",
        "Смотри как далеко ты зашёл за этот год 🚀"
    )

    private val defaultMessages = listOf(
        "Не забывай о своём плане! 📌",
        "Ты можешь это сделать! 💪",
        "Продолжай двигаться к цели! 🎯",
        "Ты молодец, не останавливайся! 🔥",
        "Каждый шаг важен. Вперёд! ⚡"
    )

    fun getMessage(repeatType: PlanRepeatType): String {
        val messages = when (repeatType) {
            PlanRepeatType.DAILY   -> dailyMessages
            PlanRepeatType.WEAKLY  -> weeklyMessages
            PlanRepeatType.MONTHLY -> monthlyMessages
            PlanRepeatType.YEARLY  -> yearlyMessages
            PlanRepeatType.NONE    -> defaultMessages
        }
        return messages.random()
    }
}