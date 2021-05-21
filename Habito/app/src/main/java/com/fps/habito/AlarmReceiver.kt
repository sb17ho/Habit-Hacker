package com.fps.habito

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        println("REST RECEIVED")
        println(MainActivity.habits)

        for (h in MainActivity.habits){
            h.progress = 0
            h.status = HabitStatus.NOT_STARTED
        }
        MainActivity.habitAdapter.notifyDataSetChanged()

    }
}