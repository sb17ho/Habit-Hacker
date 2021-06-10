package com.fps.habito

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
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

    private val descLL: LinearLayout by lazy { findViewById(R.id.descLL) }
    private val reminderLL: LinearLayout by lazy { findViewById(R.id.reminderLL) }

    private val hd1: View by lazy { findViewById(R.id.horizontal_divided_1) }
    private val hd3: View by lazy { findViewById(R.id.horizontal_divided_3) }

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
                fill(habit)
            }
        }

    }

    private fun fill(sourceHabit : Habit){
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

        reminder.text =
            if (sourceHabit.reminder.validate()) {
                hd3.visibility = View.VISIBLE
                reminderLL.visibility = View.VISIBLE
                sourceHabit.reminder.toString()
            } else {
                hd3.visibility = View.GONE
                reminderLL.visibility = View.GONE
                "Reminder not set"
            }

        streak.text = sourceHabit.stats.streak.toString()

        alltime.text = sourceHabit.stats.allTime.toString()

        comp.text = sourceHabit.stats.comp.toString()

        val dateTime = "${sourceHabit.stats.startDate}".split(" ").toTypedArray()
        startDate.text = "Created on ${dateTime[0]} ${dateTime[1]} ${dateTime[2]}"

    }


    override fun onBackPressed() {

        val mainIntent = Intent(this, MainActivity::class.java)

        fill(habit)

//        habit.desc = desc.text.toString()
//        habit.icon = if (icon.tag == null) R.drawable.nil else icon.tag.toString().toInt()
//        habit.progress.progress = intent.getParcelableExtra<Habit>("habit_info")!!.progress.progress
//
//
//        habit.progress.steps = steps.text.toString().toInt()
//        habit.stats.streak = streak.text.toString().toInt()
//        habit.stats.comp = comp.text.toString().toInt()

        mainIntent.putExtra("habit_for_main", habit)

        setResult(300, mainIntent)
        finish()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            100 -> {
                val updatedHabit = data!!.getParcelableExtra<Habit>("updated_habit")!!
                //fill(updatedHabit)

                title = updatedHabit.name

                habit.desc = updatedHabit.desc





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
        habitFormIntent.putExtra(
            "habit_filled_info",
            intent.getParcelableExtra<Habit>("habit_info")
        )
        startActivityForResult(habitFormIntent, 300)
    }

    private fun deleteHabit() {
        val mainActIntent = Intent(applicationContext, MainActivity::class.java)
        mainActIntent.putExtra("del_habit", title)
        setResult(200, mainActIntent)
        finish()
    }


}