package com.example.helperjc.notifications

import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.helperjc.enums.PlanRepeatType
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "NotificationScheduler"
    }

    fun schedule(planId: Int, title: String, endTime: LocalDateTime, repeatType: PlanRepeatType) {
        cancelNotification(planId)
        if (repeatType == PlanRepeatType.NONE) return

        val now = LocalDateTime.now()
        val duration = repeatType.toDuration()
        var next = now.plus(duration)

        while (next.isBefore(endTime)) {
            val delayMillis = ChronoUnit.MILLIS.between(now, next)
            enqueueNotification(planId, title, delayMillis, repeatType)
            next = next.plus(duration)
        }
    }

    fun scheduleTest(planId: Int, title: String, repeatType: PlanRepeatType) {
        cancelNotification(planId)
        if (repeatType == PlanRepeatType.NONE) return

        val testDuration = repeatType.toTestDuration()
        val totalNotifications = 5

        Log.d(TAG, "scheduleTest: planId=$planId, title=$title, repeatType=$repeatType")
        Log.d(
            TAG,
            "scheduleTest: testDuration=${testDuration.seconds}s, totalNotifications=$totalNotifications"
        )

        repeat(totalNotifications) { index ->
            val delayMillis = testDuration.toMillis() * (index + 1)
            Log.d(TAG, "scheduleTest: enqueue #${index + 1} with delay=${delayMillis / 1000}s")
            enqueueNotification(planId, title, delayMillis, repeatType)
        }

        Log.d(TAG, "scheduleTest: all $totalNotifications notifications enqueued")
    }

    private fun PlanRepeatType.toTestDuration(): Duration = when (this) {
        PlanRepeatType.NONE -> Duration.ZERO
        PlanRepeatType.DAILY -> Duration.ofSeconds(10)
        PlanRepeatType.WEAKLY -> Duration.ofSeconds(20)
        PlanRepeatType.MONTHLY -> Duration.ofSeconds(30)
        PlanRepeatType.YEARLY -> Duration.ofSeconds(40)
    }

    private fun PlanRepeatType.toDuration(): Duration = when (this) {
        PlanRepeatType.NONE -> Duration.ZERO
        PlanRepeatType.DAILY -> Duration.ofDays(1)
        PlanRepeatType.WEAKLY -> Duration.ofDays(7)
        PlanRepeatType.MONTHLY -> Duration.ofDays(30)
        PlanRepeatType.YEARLY -> Duration.ofDays(365)
    }

    fun cancelNotification(planId: Int) {
        WorkManager.getInstance(context).cancelAllWorkByTag(planId.toTag())
    }

    private fun enqueueNotification(
        planId: Int,
        title: String,
        delayMillis: Long,
        repeatType: PlanRepeatType
    ) {
        val request = OneTimeWorkRequestBuilder<PlanNotificationWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    "title" to title,
                    "planId" to planId,
                    "repeatType" to repeatType.name
                )
            )
            .addTag(planId.toTag())
            .build()
        WorkManager.getInstance(context).enqueue(request)
    }

    private fun Int.toTag() = "plan_$this"

}


