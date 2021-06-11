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
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts

class InfoActivity : AppCompatActivity() {

    private val icon: ImageView by lazy { findViewById(R.id.imageView) }
    private val habitName: TextView by lazy { findViewById(R.id.habitName) }
    private val desc: TextView by lazy { findViewById(R.id.desc) }
    private val steps: TextView by lazy { findViewById(R.id.steps) }
    private val reminder: TextView by lazy { findViewById(R.id.reminder) }
    private val streak: TextView by lazy { findViewById(R.id.streakValue) }
    private val allTime: TextView by lazy { findViewById(R.id.alltimeValue) }
    private val comp: TextView by lazy { findViewById(R.id.compValue) }
    private val startDate: TextView by lazy { findViewById(R.id.startDate) }

    private val descLL: LinearLayout by lazy { findViewById(R.id.descLL) }
    private val reminderLL: LinearLayout by lazy { findViewById(R.id.setReminderLL) }

    private val hd1: View by lazy { findViewById(R.id.horizontal_divided_1) }
    private val hd3: View by lazy { findViewById(R.id.horizontal_divided_3) }

    private lateinit var habit: Habit

    private val resultContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

        when (it.resultCode) {
            100 -> {
                habit = it.data!!.getParcelableExtra("updated_habit")!!
                fillViews(habit)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_info)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.primary_pink)))
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.primary_pink)

        when (intent.getStringExtra("PARENT_ACTIVITY_NAME")) {
            "MAIN" -> {
                habit = intent.getParcelableExtra("habit_info")!!
                fillViews(habit)
            }
        }

    }

    private fun fillViews(sourceHabit: Habit) {

        title = sourceHabit.name

        icon.setImageResource(sourceHabit.icon)
        icon.tag = sourceHabit.icon

        habitName.text = sourceHabit.name

        desc.text = sourceHabit.desc
        if (desc.text.isNotEmpty()) {
            hd1.visibility = View.VISIBLE
            descLL.visibility = View.VISIBLE
        } else {
            hd1.visibility = View.GONE
            descLL.visibility = View.GONE
        }

        steps.text = sourceHabit.progress.steps.toString()

        if (sourceHabit.reminder.validate()) {
            hd3.visibility = View.VISIBLE
            reminderLL.visibility = View.VISIBLE
            reminder.text = sourceHabit.reminder.toString()
        } else {
            hd3.visibility = View.GONE
            reminderLL.visibility = View.GONE
        }

        streak.text = sourceHabit.stats.streak.toString()

        allTime.text = sourceHabit.stats.allTime.toString()

        comp.text = sourceHabit.stats.comp.toString()

        val dateTime = "${sourceHabit.stats.startDate}".split(" ").toTypedArray()
        val startDateTime = "Created on ${dateTime[0]} ${dateTime[1]} ${dateTime[2]}"
        startDate.text = startDateTime

    }

    override fun onBackPressed() {

        val mainIntent = Intent(this, MainActivity::class.java)

        fillViews(habit)

        mainIntent.putExtra("habit_for_main", habit)

        setResult(300, mainIntent)
        finish()

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
        habitFormIntent.putExtra("habit_filled_info", habit)
        resultContract.launch(habitFormIntent)

    }

    private fun deleteHabit() {
        val mainActIntent = Intent(applicationContext, MainActivity::class.java)
        mainActIntent.putExtra("del_habit", title)
        setResult(200, mainActIntent)
        finish()
    }


}