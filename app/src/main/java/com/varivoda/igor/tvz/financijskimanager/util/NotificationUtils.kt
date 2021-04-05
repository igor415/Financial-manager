package com.varivoda.igor.tvz.financijskimanager.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.ui.stock.StockActivity

fun NotificationManager.sendNotification(text: String, context: Context, content: String){
    val intent = Intent(context,StockActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    intent.putExtra("key","stockDataFragment")

    val contentPendingIntent = PendingIntent.getActivity(
        context,
        NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher_foreground)
        .setContentTitle(text)
        .setContentText(content)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setSound(sound)
        /*.setLargeIcon(
            getImage(
                context
            )
        )
        .setStyle(
            getImageStyle(
                context
            )
        )*/
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