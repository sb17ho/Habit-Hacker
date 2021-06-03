package com.fps.habito

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

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

    private val collectionRef = MainActivity.firebaseAccess.firebaseDatabase.collection("Habit")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_info)

        when (intent.getStringExtra("PARENT_ACTIVITY_NAME")) {
            "MAIN" -> {
                fillViews()
            }
        }

    }

    override fun onBackPressed() {
        finish()
    }

    private fun fillViews() {

        collectionRef
            .document(intent.getStringExtra("habit_name")!!)
            .get()
            .addOnSuccessListener {

                val currentHabit = it.toObject(Habit::class.java)!!

                title = currentHabit.name

                habitName.text = currentHabit.name

                desc.text = currentHabit.desc
                if (currentHabit.desc.isNotEmpty()) {
                    desc.visibility = View.VISIBLE
                }

                icon.setImageResource(currentHabit.icon)
                icon.tag = currentHabit.icon

                steps.text = currentHabit.progress.steps.toString()

                reminder.text =
                    if (currentHabit.reminder.isSet()) {
                        reminder.visibility = View.VISIBLE
                        currentHabit.reminder.toString()
                    } else {
                        "Reminder not set"
                    }

                streak.text = currentHabit.stats.streak.toString()
                comp.text = currentHabit.stats.comp.toString()
                allTime.text = "0"

                startDate.text = currentHabit.stats.startDate.toString()

            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 300) {
            fillViews()
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
        }

        return super.onOptionsItemSelected(item)
    }

    private fun editHabit() {
        val habitFormIntent = Intent(applicationContext, FormActivity::class.java)
        habitFormIntent.putExtra("PARENT_ACTIVITY_NAME", "HABIT_INFO")
        habitFormIntent.putExtra("edit_habit", title)
        startActivityForResult(habitFormIntent, 300)
    }

    private fun deleteHabit() {
        val mainActIntent = Intent(applicationContext, MainActivity::class.java)
        mainActIntent.putExtra("del_habit", title)
        setResult(200, mainActIntent)
        finish()
    }

}











