package com.fps.habito

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity


/**
 * TODO figure out a way to pass Set to ArrayAdapter
 */

class MainActivity : AppCompatActivity() {

    private val habitsGrid: GridView by lazy { findViewById(R.id.habitsGrid) }
    private val add: Button by lazy { findViewById(R.id.add) }
    private val remove: Button by lazy { findViewById(R.id.remove) }

    private var habits = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        add.setOnClickListener {
            val habitFormIntent = Intent(this, HabitForm::class.java)
            val result = 1
            startActivityForResult(habitFormIntent, result)
        }

        habitDeletion()

        habitsGrid.onItemLongClickListener = OnItemLongClickListener { arg0, arg1, position, arg3 ->
            val habitInfoIntent = Intent(this, HabitInfo::class.java)
            startActivity(habitInfoIntent)
            true
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // adds the new habit to the main habit grid
        val newHabitName = data?.getStringExtra("new_habit_name").toString()
        if (!habits.contains(newHabitName)) {
            habits.add(newHabitName)
            habitsGrid.adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, habits)
        }
    }

    private fun habitDeletion() {
        remove.setOnClickListener {
            var deleteButtonPressed = true
            habitsGrid.onItemClickListener = OnItemClickListener { _, _, position, _ ->
                if (deleteButtonPressed) {
                    habits.removeAt(position)
                }
                habitsGrid.adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, habits)
                deleteButtonPressed = false
            }
        }
    }

}

