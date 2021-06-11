package com.fps.habito

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class HabitsReseter : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        MainActivity.habits.forEach {

            if (it.progress.status != Status.COMPLETED.toString()) {
                it.stats.streak = 0
            }

            it.progress.progress = 0
            it.progress.status = Status.NOT_STARTED.toString()

        }

        context?.sendBroadcast(Intent("NotifyAndBackup"))

    }


}

