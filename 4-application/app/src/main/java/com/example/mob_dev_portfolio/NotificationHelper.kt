package com.example.mob_dev_portfolio

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat

// Helper class to manage the diff channels
class NotificationHelper {
    companion object {
        const val WEATHER_CHANNEL_ID = "weather_alerts"
        const val MEDICATION_CHANNEL_ID = "medication_reminders"

        fun createNotificationChannels(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                val weatherChannel = NotificationChannel(
                    WEATHER_CHANNEL_ID,
                    "Weather Alerts",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Channel for weather-related alerts"
                }

                val medChannel = NotificationChannel(
                    MEDICATION_CHANNEL_ID,
                    "Medication Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Channel for medication reminder notifications"
                }

                // Create the channels
                notificationManager.createNotificationChannel(weatherChannel)
                notificationManager.createNotificationChannel(medChannel)
            }
        }
    }
}