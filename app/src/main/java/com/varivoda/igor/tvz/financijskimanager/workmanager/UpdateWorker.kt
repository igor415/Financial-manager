package com.varivoda.igor.tvz.financijskimanager.workmanager

import android.app.NotificationManager
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.util.cancelNotifications
import com.varivoda.igor.tvz.financijskimanager.util.sendNotification

class UpdateWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {
        val notificationManager = ContextCompat.getSystemService(applicationContext,
            NotificationManager::class.java) as NotificationManager
        notificationManager.cancelNotifications()
        notificationManager.sendNotification(applicationContext.getString(R.string.notification_title),applicationContext)
        return Result.success()
    }

}