package com.fps.habito

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get

/**
 * TODO figure out a way to pass Set to ArrayAdapter
 */

class MainActivity : AppCompatActivity() {

    private val habitsGrid: GridView by lazy { findViewById(R.id.habitsGrid) }
    private val add: Button by lazy { findViewById(R.id.add) }

    private var habits = ArrayList<Habit>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Starts HabitForm activity
        add.setOnClickListener {
            val habitFormIntent = Intent(this, HabitForm::class.java)
            val result = 1
            startActivityForResult(habitFormIntent, result)
        }

        openHabitInfo()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 100) { // addition
            val newHabitData = data?.getStringArrayListExtra("new_habit")!!

            val newHabit = Habit(
                    newHabitData[0],
                    newHabitData[1],
            )

            if (!habits.contains(newHabit)) {
                habits.add(newHabit)
                habitsGrid.adapter = HabitAdapter(this, habits)
            }

        } else if (resultCode == 200) { // deletion
            habits.removeIf { it.name == data!!.getStringExtra("del_habit") }
            habitsGrid.adapter = HabitAdapter(this, habits)
        }

    }

    private fun openHabitInfo() {

        habitsGrid.onItemLongClickListener = OnItemLongClickListener { a, b, c, d ->
            val habitInfoIntent = Intent(this, HabitInfo::class.java)

            println("GRID ${habits[c]}")
            habitInfoIntent.putStringArrayListExtra("habit_info",
                    arrayListOf(
                            habits[c].name,
                            habits[c].desc,
                            habits[c].steps.toString(),
                            habits[c].streak.toString(),
                            habits[c].allTime.toString(),
                            habits[c].comp.toString(),
                    ))
            val result = 2
            startActivityForResult(habitInfoIntent, result)
            true
        }

    }


}

