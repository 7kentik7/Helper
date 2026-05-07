package com.example.helperjc.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

object NotificationChannelInitializer {

    fun init(context: Context) {
        val channel = NotificationChannel(
            "plan_channel",
            "Планы",
            NotificationManager.IMPORTANCE_HIGH
        ).apply { description = "Напоминания о планах" }

        context.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }
}