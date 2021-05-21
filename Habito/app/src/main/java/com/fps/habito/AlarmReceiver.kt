package com.fps.habito

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        println("REST RECEIVED")
        println(MainActivity.habits)

        for (h in MainActivity.habits){
            h.name = "simar"
            h.progress = 0
            h.status = "NOT_STARTED"
        }
        MainActivity.habitAdapter.notifyDataSetChanged()

//        Toast.makeText(context, "param", Toast.LENGTH_LONG).show()
    }
}