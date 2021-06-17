package com.fps.habito

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*


class ReminderNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val notificationManagerCompat = NotificationManagerCompat.from(context!!)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.google_icon)
            .setContentTitle("Reminder")
            .setContentText("Time to complete your habit")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .build()

        notificationManagerCompat.notify(
            Calendar.getInstance().timeInMillis.toInt(),
            notification
        )

    }

}