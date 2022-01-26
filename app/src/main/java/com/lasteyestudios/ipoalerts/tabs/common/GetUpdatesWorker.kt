package com.lasteyestudios.ipoalerts.tabs.common

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lasteyestudios.ipoalerts.repository.NotificationRepo

class GetUpdatesWorker(context: Context, param: WorkerParameters) :
    CoroutineWorker(context, param) {
    private val notificationRepo = NotificationRepo.getInstance()
    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {
        return try {
            notificationRepo.getIPOCompanyListingsForNotifications()
            Result.success()

        } catch (e: Exception) {
            Result.failure()
        }
    }
}