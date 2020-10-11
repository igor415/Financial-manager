package com.varivoda.igor.tvz.financijskimanager.workmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.*

class BroadcastNotification : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val workManager = WorkManager.getInstance(context!!)
        val workId = UUID.randomUUID().toString()

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val request = OneTimeWorkRequestBuilder<UpdateWorker>()
            .setConstraints(constraints)
            .build()

        val work = workManager
            .beginUniqueWork(
                workId,
                ExistingWorkPolicy.REPLACE,
                request
            )

        work.enqueue()


    }

}