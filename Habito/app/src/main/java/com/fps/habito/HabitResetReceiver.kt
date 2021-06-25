package com.fps.habito

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.CalendarView
import android.widget.Toast
import java.util.*

class HabitResetReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context!!.sendBroadcast(Intent("NotifyAndBackup"))
    }

}

