package com.eyther.lumbridge.platform.workers.delegate

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.eyther.lumbridge.platform.workers.CheckPendingPaymentsWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SetupWorkersDelegate @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    companion object {
        private const val TAG = "SetupWorkersDelegate"
    }

    fun setupWorkers() = runCatching {
        setupRecurringPaymentsWorker()
    }.getOrElse {
        Log.e(TAG, "\uD83D\uDCA5 Failed to setup workers: $it")
    }

    private fun setupRecurringPaymentsWorker() {
        Log.d(TAG, "\uD83D\uDCAC Setting up recurring payments worker")

        val workerTag = "CheckPendingPaymentsWork"

        // Calculate the initial delay to run at midnight
        val currentTime = LocalTime.now()
        val midnight = LocalTime.MIDNIGHT

        // If the current time is after midnight, calculate the delay until the next midnight.
        val initialDelay = if (currentTime.isAfter(midnight)) {
            Duration.between(currentTime, midnight.plusHours(24))
        } else {
            Duration.between(currentTime, midnight)
        }

        val constraints = Constraints.Builder()
            .setRequiresStorageNotLow(true)
            .build()

        // Run the worker every day at midnight
        val workRequest = PeriodicWorkRequestBuilder<CheckPendingPaymentsWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelay.toMillis(), TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                workerTag,
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )

        Log.d(TAG, "\uD83D\uDE80 Recurring payments worker set up")
    }
}