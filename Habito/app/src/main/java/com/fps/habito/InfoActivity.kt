package com.fps.habito

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView

class InfoActivity : AppCompatActivity() {

    private val icon: ImageView by lazy { findViewById(R.id.imageView) }
    private val habitName: TextView by lazy { findViewById(R.id.habitName) }
    private val desc: TextView by lazy { findViewById(R.id.desc) }
    private val steps: TextView by lazy { findViewById(R.id.steps) }
    private val reminder: TextView by lazy { findViewById(R.id.reminder) }
    private val streak: TextView by lazy { findViewById(R.id.streakValue) }
    private val alltime: TextView by lazy { findViewById(R.id.alltimeValue) }
    private val comp: TextView by lazy { findViewById(R.id.compValue) }
    private val startDate: TextView by lazy { findViewById(R.id.startDate) }

    private lateinit var habit: Habit

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_info)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.primary_pink)))
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.statusBarColor = resources.getColor(R.color.primary_pink)

        when (intent.getStringExtra("PARENT_ACTIVITY_NAME")) {
            "MAIN" -> {
                habit = intent.getParcelableExtra("habit_info")!!
                fillViews()
            }
        }

    }

    private fun fillViews() {

        title = habit.name

        icon.setImageResource(habit.icon)
        icon.tag = habit.icon
        habitName.text = habit.name

        desc.text = habit.desc
        if (desc.text.isNotEmpty()) {
            desc.visibility = View.VISIBLE
        }

        steps.text = habit.progress.steps.toString()

        reminder.text =
            if (habit.reminder.isSet()) {
                reminder.visibility = View.VISIBLE
                habit.reminder.toString()
            } else {
                "Reminder not set"
            }

        streak.text = habit.stats.streak.toString()

        alltime.text = habit.stats.allTime.toString()

        comp.text = habit.stats.comp.toString()

        startDate.text = "Started on ${habit.stats.startDate}"

    }

    override fun onBackPressed() {

        val mainIntent = Intent(this, MainActivity::class.java)

        habit.desc = desc.text.toString()
        habit.icon = if (icon.tag == null) R.drawable.nil else icon.tag.toString().toInt()
        habit.reminder = Reminder(5, 9, "pm")
        habit.progress.progress = intent.getParcelableExtra<Habit>("habit_info")!!.progress.progress
        habit.progress.steps = steps.text.toString().toInt()
        habit.stats.streak = streak.text.toString().toInt()
        habit.stats.comp = comp.text.toString().toInt()

        mainIntent.putExtra("habit_for_main", habit)

        setResult(300, mainIntent)
        finish()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            100 -> {
                val updatedHabit = data!!.getParcelableExtra<Habit>("updated_habit")!!

                title = habitName.text

                habitName.text = updatedHabit.name
                icon.tag = updatedHabit.icon
                icon.setImageResource(updatedHabit.icon)

                desc.text = updatedHabit.desc
                if (desc.text.isNotEmpty()) {
                    desc.visibility = View.VISIBLE
                }

                steps.text = updatedHabit.progress.steps.toString()

                reminder.text =
                    if (updatedHabit.reminder.isSet()) {
                        reminder.visibility = View.VISIBLE
                        updatedHabit.reminder.toString()
                    } else {
                        "Reminder not set"
                    }

                streak.text = updatedHabit.stats.streak.toString()
                alltime.text = updatedHabit.stats.allTime.toString()
                comp.text = updatedHabit.stats.comp.toString()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.habit_info_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.editMenuOption -> editHabit()
            R.id.deleteMenuOption -> deleteHabit()
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun editHabit() {
        val habitFormIntent = Intent(applicationContext, FormActivity::class.java)
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