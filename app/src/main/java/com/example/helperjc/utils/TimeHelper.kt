package com.example.helperjc.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


fun LocalDateTime.toTimeInMillis(): Long = atZone(ZoneId.systemDefault())
    .toInstant()
    .toEpochMilli()
fun YearMonth.toEpochMonth(): Long = year * 12L + (monthValue - 1)

fun Long.toYearMonth(): YearMonth =
    YearMonth.of((this / 12).toInt(), (this % 12).toInt() + 1)
fun Long.toLocalDateTime(): LocalDateTime = Instant.ofEpochMilli(this)
    .atZone(ZoneId.systemDefault())
    .toLocalDateTime()

fun LocalDateTime.parseToString(): String {
    val formatter = DateTimeFormatter.ofPattern("d MMM yyyy")
    return this.format(formatter)
}

fun String.parseToLocalDateTime(): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale("ru"))
    return LocalDate.parse(this, formatter).atStartOfDay()
}

fun Long.longToStringFormattedDate(): String {
    return this.toLocalDateTime().parseToString()
}