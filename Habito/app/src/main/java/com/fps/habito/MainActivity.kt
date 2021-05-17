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
    private val remove: Button by lazy { findViewById(R.id.remove) }

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

        habitDeletion()

        openHabitInfo()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val newHabitData = data?.getStringArrayListExtra("new_habit")!!

        // adds the new habit to the main habit grid
        val newHabit = Habit(
            newHabitData[0],
            newHabitData[1],
        )

        if (!habits.contains(newHabit)) {
            habits.add(newHabit)
            habitsGrid.adapter = HabitAdapter(this, habits)

        }
    }

    private fun openHabitInfo(){

        habitsGrid.onItemLongClickListener = OnItemLongClickListener { _, _, _, _ ->
            val habitInfoIntent = Intent(this, HabitInfo::class.java)
            startActivity(habitInfoIntent)
            true
        }

    }

    private fun habitDeletion() {
        remove.setOnClickListener {
            var deleteButtonPressed = true
            habitsGrid.onItemClickListener = OnItemClickListener { _, _, position, _ ->
                if (deleteButtonPressed) {
                    habits.removeAt(position)
                }
               habitsGrid.adapter = HabitAdapter(this, habits)
                deleteButtonPressed = false
            }
        }
    }

}

