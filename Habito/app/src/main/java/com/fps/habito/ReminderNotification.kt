package com.fps.habito

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager

class ReminderNotification : Application() {

    companion object {
        val CHANNEL_ID = "channel_id"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {

        val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Habit reminder",
                NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.description = "Time to complete your habit"

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)

    }


}