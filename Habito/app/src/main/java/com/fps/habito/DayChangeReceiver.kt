package com.fps.habito

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class DayChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        MainActivity.habits.forEach {

            if (it.progress.status != Status.COMPLETED.toString()) {
                it.stats.streak = 0
            }

            it.progress.progress = 0
            it.progress.status = Status.NOT_STARTED.toString()

        }

        MainActivity.habitAdapter.notifyDataSetChanged()

        MainActivity.habits.forEach {
            MainActivity.firestoreConnection
                .collection("Habits")
                .document(it.name).set(it)
                .addOnSuccessListener { println("success update to firestore at midnight") }
                .addOnFailureListener { println("failed updation to firestore at midnight") }
        }

    }


}

