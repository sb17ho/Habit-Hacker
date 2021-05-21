package com.fps.habito

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.*
import android.widget.AdapterView.OnItemLongClickListener
import androidx.appcompat.app.AppCompatActivity

import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val habitsGrid: GridView by lazy { findViewById(R.id.habitsGrid) }
    private val add: ImageView by lazy { findViewById(R.id.add) }

    private var habits = ArrayList<Habit>()
    private lateinit var habitAdapter: HabitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        habitAdapter = HabitAdapter(this, habits)
        habitsGrid.adapter = habitAdapter

        add.setOnClickListener {
            val habitFormIntent = Intent(this, HabitFormActivity::class.java)
            habitFormIntent.putExtra("PARENT_ACTIVITY_NAME", "MAIN")
            startActivityForResult(habitFormIntent, 1)
        }

        progressHabit()
        openHabitInfo()
    }

    private fun progressHabit() {
        habitsGrid.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            habits[position].updateProgress()
            println("HABIT DATA ${habits[position]}")
        }
    }

    /**
     * Starts HabitInfoActivity
     */
    private fun openHabitInfo() {

        habitsGrid.onItemLongClickListener = OnItemLongClickListener { a, b, position, d ->
            val habitInfoIntent = Intent(this, HabitInfoActivity::class.java)
            habitInfoIntent.putExtra("PARENT_ACTIVITY_NAME", "MAIN")
            habitInfoIntent.putExtra("habit_info", habits[position])
            startActivityForResult(habitInfoIntent, 2)
            true
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        /**
         * 100 for addition
         * 200 for deletion
         * 400 for back-buttoning from Info to Main
         */

        if (resultCode == 100) {
            val newHabit = data?.getParcelableExtra<Habit>("new_habit")!!

            if (!habits.contains(newHabit)) {
                println("HELLO")
                habitAdapter.add(newHabit)
                habitAdapter.notifyDataSetChanged()
            }

        } else if (resultCode == 200) {
            habitAdapter.remove(habits.find { it.name == data!!.getStringExtra("del_habit") })
            habitAdapter.notifyDataSetChanged()
        } else if (resultCode == 400) {

            val updatedHabit = data!!.getParcelableExtra<Habit>("habit_for_main")

            val targetHabit = habits.find { it.name == data.getStringExtra("old_habit_for_main") }
            targetHabit?.icon = updatedHabit?.icon!!
            targetHabit?.name = updatedHabit.name
            targetHabit?.desc = updatedHabit.desc
            targetHabit?.steps = updatedHabit.steps
            targetHabit?.streak = updatedHabit.streak
            targetHabit?.allTime = updatedHabit.allTime
            targetHabit?.comp = updatedHabit.comp

            habitAdapter.notifyDataSetChanged()

        }

    }

}

