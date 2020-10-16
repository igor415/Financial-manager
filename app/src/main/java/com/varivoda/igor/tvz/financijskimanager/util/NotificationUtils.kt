package com.varivoda.igor.tvz.financijskimanager.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity

fun NotificationManager.sendNotification(text: String, context: Context){
    val intent = Intent(context,HomeActivity::class.java)

    val contentPendingIntent = PendingIntent.getActivity(
        context,
        NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(text)
        .setContentText("")
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setLargeIcon(
            getImage(
                context
            )
        )
        .setStyle(
            getImageStyle(
                context
            )
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    val pref = Preferences(context)
    if(pref.getVibrationsOption()) builder.setVibrate(LongArray(300))

    notify(NOTIFICATION_ID,builder.build())
}

fun getImage(context: Context): Bitmap? = BitmapFactory.decodeResource(context.resources,
    R.drawable.database_updated
)

fun NotificationManager.cancelNotifications() {
    cancelAll()
}

fun getImageStyle(context: Context): NotificationCompat.BigPictureStyle? {
    val image = getImage(context)
    return NotificationCompat.BigPictureStyle()
        .bigPicture(image)
        .bigLargeIcon(null)
}