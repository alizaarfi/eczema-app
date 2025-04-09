package com.example.mob_dev_portfolio

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationUtils {

    fun showNotification(context: Context, channelId: String, title: String, message: String, notifId: Int) {
        try {
            // creating and sending
            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(androidx.core.R.drawable.notification_icon_background)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            with(NotificationManagerCompat.from(context)) {
                notify(notifId, builder.build())
            }
        } catch (e: SecurityException) {
            // Handle the security exception error
            e.printStackTrace()
        }
    }

}