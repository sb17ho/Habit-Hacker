package com.fps.habito

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.constraintlayout.solver.state.State
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.*
import java.util.concurrent.TimeUnit

class HabitInfoActivity : AppCompatActivity() {

    private val icon: ImageView by lazy { findViewById(R.id.imageView) }
    private val habitName: TextView by lazy { findViewById(R.id.habitName) }
    private val desc: TextView by lazy { findViewById(R.id.desc) }
    private val steps: TextView by lazy { findViewById(R.id.steps) }
    private val reminder: TextView by lazy { findViewById(R.id.reminder) }
    private val streak: TextView by lazy { findViewById(R.id.streakValue) }
    private val alltime: TextView by lazy { findViewById(R.id.alltimeValue) }
    private val comp: TextView by lazy { findViewById(R.id.compValue) }
    private val startDate: TextView by lazy { findViewById(R.id.startDate) }

    private val oldHabitName by lazy { intent.getParcelableExtra<Habit>("habit_info")!!.name }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_info)

        if (intent.getStringExtra("PARENT_ACTIVITY_NAME").equals("MAIN")) {
            fillFormFields()
            title = habitName.text
        }

    }

    override fun onBackPressed() {

        val mainIntent = Intent(this, MainActivity::class.java)

        mainIntent.putExtra("habit_for_main",
                Habit(
                        if (icon.tag == null) R.drawable.nil else icon.tag.toString().toInt(),
                        title.toString(),
                        desc.text.toString(),
                        steps.text.toString().toInt(),
                        HabitStats(streak.text.toString().toInt(),comp.text.toString().toInt()),
                        HabitReminder(1, 1, "am")
                )
        )

        mainIntent.putExtra("old_habit_for_main", oldHabitName)

        setResult(400, mainIntent)
        finish()

    }

    private fun fillFormFields() {

        val habit = intent.getParcelableExtra<Habit>("habit_info")!!

        icon.setImageResource(habit.icon)
        icon.tag = habit.icon
        habitName.text = habit.name

        desc.text = habit.desc
        if (desc.text.isNotEmpty()) {
            desc.visibility = View.VISIBLE
        }

        steps.text = habit.steps.toString()

        reminder.text = if (habit.habitReminder.isSet()) {
            reminder.visibility = View.VISIBLE
            habit.habitReminder.toString()
        } else {
            "Reminder not set"
        }

        streak.text = habit.habitStats.streak.toString()

        val timeElapsed = if (TimeUnit.DAYS.convert(Calendar.getInstance().time.time - habit.startDate.time, TimeUnit.MILLISECONDS) == 0L) {
            1
        } else {
            TimeUnit.DAYS.convert(Calendar.getInstance().time.time - habit.startDate.time, TimeUnit.MILLISECONDS)
        }

        alltime.text = ((habit.habitStats.comp*1.0)/timeElapsed).toString()

        comp.text = habit.habitStats.comp.toString()

        startDate.text = "Started on ${habit.startDate}"


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 300) {
            val updatedHabit = data!!.getParcelableExtra<Habit>("updated_habit")!!

            icon.setImageResource(updatedHabit.icon)
            icon.tag = updatedHabit.icon
            habitName.text = updatedHabit.name

            desc.text = updatedHabit.desc
            if (desc.text.isNotEmpty()) {
                desc.visibility = View.VISIBLE
            }

            steps.text = updatedHabit.steps.toString()

            reminder.text = if (updatedHabit.habitReminder.isSet()) {
                reminder.visibility = View.VISIBLE
                updatedHabit.habitReminder.toString()
            } else {
                "Reminder not set"
            }

            streak.text = updatedHabit.habitStats.streak.toString()

            val timeElapsed = if (TimeUnit.DAYS.convert(Calendar.getInstance().time.time - updatedHabit.startDate.time, TimeUnit.MILLISECONDS) == 0L) {
                1
            } else {
                TimeUnit.DAYS.convert(Calendar.getInstance().time.time - updatedHabit.startDate.time, TimeUnit.MILLISECONDS)
            }

            alltime.text = ((updatedHabit.habitStats.comp*1.0)/timeElapsed).toString()

            comp.text = updatedHabit.habitStats.comp.toString()

            title = habitName.text
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.habit_info_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.editMenuOption) {
            editHabit()
        } else if (item.itemId == R.id.deleteMenuOption) {
            deleteHabit()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun editHabit() {
        val habitFormIntent = Intent(applicationContext, HabitFormActivity::class.java)
        habitFormIntent.putExtra("PARENT_ACTIVITY_NAME", "HABIT_INFO")
        habitFormIntent.putExtra("habit_filled_info", intent.getParcelableExtra<Habit>("habit_info"))
        startActivityForResult(habitFormIntent, 300)
    }

    private fun deleteHabit() {
        val mainActIntent = Intent(applicationContext, MainActivity::class.java)
        mainActIntent.putExtra("del_habit", title)
        setResult(200, mainActIntent)
        finish()
    }

}