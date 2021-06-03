package com.fps.habito

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DayChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        intent?.getParcelableArrayListExtra<Habit>("all_habits")?.forEach {

            if (it.progress.status != Status.COMPLETED.toString()) {
                it.stats.streak = 0
            }

            it.progress.progress = 0
            it.progress.status = Status.NOT_STARTED.toString()

        }

        MainActivity.habitAdapter.notifyDataSetChanged()

    }

}