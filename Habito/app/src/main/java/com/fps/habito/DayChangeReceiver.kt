package com.fps.habito

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.*

class DayChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

       Log.d("DayChangeReceiver:onReceive", "Day changed at ${Calendar.getInstance().time}")

        MainActivity.habits.forEach {

            Log.d("DayChangeReceiver:onReceive", it.toString())

            if (it.progress.status != Status.COMPLETED.toString()) {
                Log.d("DayChangeReceiver:onReceive", "Streak ended")
                it.stats.streak = 0
            }

            it.progress.progress = 0
            it.progress.status = Status.NOT_STARTED.toString()


        }

        MainActivity.habitAdapter.notifyDataSetChanged()

    }

}