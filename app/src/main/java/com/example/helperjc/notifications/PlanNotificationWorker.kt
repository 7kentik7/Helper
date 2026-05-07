package com.example.helperjc.notifications

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.helperjc.R
import com.example.helperjc.enums.PlanRepeatType
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class PlanNotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val title = inputData.getString("title") ?: return Result.failure()
        val planId = inputData.getInt("planId", -1)
        val repeatType = inputData.getString("repeatType")
            ?.let { PlanRepeatType.valueOf(it) }
            ?: PlanRepeatType.NONE

        val message = NotificationTextProvider.getMessage(repeatType)

        val notification = NotificationCompat.Builder(applicationContext, "plan_channel")
            .setSmallIcon(R.drawable.tasks_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                applicationContext, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(applicationContext)
                .notify(planId, notification)
        }
        return Result.success()
    }
}