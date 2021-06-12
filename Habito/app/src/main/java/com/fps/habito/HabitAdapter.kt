package com.fps.habito

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.progressindicator.LinearProgressIndicator

class HabitAdapter(private var mContext: Context, private var allHabits: ArrayList<Habit>) :
    ArrayAdapter<Habit>(mContext, 0, allHabits) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var habitItemView: View? = convertView

        if (habitItemView == null) {
            habitItemView =
                LayoutInflater.from(mContext).inflate(R.layout.habit_view, parent, false)
        }

        val habit = allHabits[position]

        (habitItemView!!.findViewById(R.id.habitViewName) as TextView).text = habit.name
        (habitItemView.findViewById(R.id.habitIcon) as ImageView).setImageResource(habit.icon)
        (habitItemView.findViewById(R.id.streakView) as TextView).text =
            habit.stats.streak.toString()
        (habitItemView.findViewById(R.id.progressBar) as LinearProgressIndicator).progress =
            ((habit.progress.progress * 1.0 / habit.progress.steps) * 100).toInt()

        return habitItemView

    }
}