package com.example.helperjc

import android.text.Editable
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


fun LocalDateTime.toTimeInMillis(): Long = atZone(ZoneId.systemDefault())
    .toInstant()
    .toEpochMilli()

fun Long.toLocalDateTime(): LocalDateTime = Instant.ofEpochMilli(this)
    .atZone(ZoneId.systemDefault())
    .toLocalDateTime()

fun LocalDateTime.parseToString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    return this.format(formatter)
}
fun String.parseToLocalDateTime(): LocalDateTime {
    // Используем ТОТ ЖЕ шаблон, но обязательно указываем Locale,
    // чтобы корректно парсились названия месяцев (например, "Dec")
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
    // Сначала парсим в LocalDate (так как времени нет), затем добавляем начало суток
    return LocalDate.parse(this, formatter).atStartOfDay()
}