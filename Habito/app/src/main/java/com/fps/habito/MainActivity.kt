package com.fps.habito

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.Button
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import com.fps.habito.R.drawable

class MainActivity : AppCompatActivity() {

    private val habitsGrid: GridView by lazy { findViewById(R.id.habitsGrid) }
    private val add: Button by lazy { findViewById(R.id.add) }

    private var habits = ArrayList<Habit>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        add.setOnClickListener {
            val habitFormIntent = Intent(this, HabitFormActivity::class.java)
            habitFormIntent.putExtra("PARENT_ACTIVITY_NAME", "MAIN")
            startActivityForResult(habitFormIntent, 1)
        }

        openHabitInfo()
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

        println("RESULT $resultCode")

        if (resultCode == 100) { // addition
            val newHabit = data?.getParcelableExtra<Habit>("new_habit")!!

            if (!habits.contains(newHabit)) {
                habits.add(newHabit)
                habitsGrid.adapter = HabitAdapter(this, habits)
            }

        } else if (resultCode == 200) { // deletion
            habits.removeIf { it.name == data!!.getStringExtra("del_habit") }
            habitsGrid.adapter = HabitAdapter(this, habits)
        } else if (resultCode == 400) { // returning to main with no changes

            val updatedHabit = data!!.getParcelableExtra<Habit>("habit_for_main")

            val targetHabit = habits.find { it.name == data.getStringExtra("old_habit_for_main") }
            targetHabit?.icon = updatedHabit?.icon!!
            targetHabit?.name = updatedHabit.name
            targetHabit?.desc = updatedHabit.desc
            targetHabit?.steps = updatedHabit.steps
            targetHabit?.streak = updatedHabit.streak
            targetHabit?.allTime = updatedHabit.allTime
            targetHabit?.comp = updatedHabit.comp

            habitsGrid.adapter = HabitAdapter(this, habits)

        }

    }

}

