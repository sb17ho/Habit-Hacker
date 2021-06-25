package com.fps.habito

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

const val CHANNEL_ID = "channel0"

class ReminderNotification : Application() {

    override fun onCreate() {
        super.onCreate()

        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            "First notification",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.description = "Time to complete your habit"

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)


    }

}

