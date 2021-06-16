package com.fps.habito

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ReminderNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val notification =
            NotificationCompat.Builder(context!!, ReminderNotification.CHANNEL_ID)
                .setSmallIcon(R.id.habitIcon)
                .setContentTitle(intent!!.getStringExtra("habit_reminder_for"))
                .setContentText(intent.getStringExtra("habit_reminder_for"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .build()

        NotificationManagerCompat
            .from(context)
            .notify(System.currentTimeMillis().toInt(), notification)

    }

}