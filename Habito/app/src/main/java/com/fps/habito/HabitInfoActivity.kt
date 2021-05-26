package com.fps.habito

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView

class HabitInfoActivity : AppCompatActivity() {

    private val icon: ImageView by lazy { findViewById(R.id.imageView) }
    private val habitName: TextView by lazy { findViewById(R.id.habitName) }
    private val desc: TextView by lazy { findViewById(R.id.desc) }
    private val steps: TextView by lazy { findViewById(R.id.steps) }
    private val streak: TextView by lazy { findViewById(R.id.streakValue) }
    private val alltime: TextView by lazy { findViewById(R.id.alltimeValue) }
    private val comp: TextView by lazy { findViewById(R.id.compValue) }

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
                        icon.tag.toString().toInt(),
                        title.toString(),
                        desc.text.toString(),
                        steps.text.toString().toInt(),
                        streak.text.toString().toInt(),
                        alltime.text.toString().toDouble(),
                        comp.text.toString().toInt(),
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
        steps.text = habit.steps.toString()
        streak.text = habit.streak.toString()
        alltime.text = habit.allTime.toString()
        comp.text = habit.comp.toString()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 300) {
            val updatedHabit = data!!.getParcelableExtra<Habit>("updated_habit")!!
            icon.setImageResource(updatedHabit.icon)
            icon.tag = updatedHabit.icon
            habitName.text = updatedHabit.name
            desc.text = updatedHabit.desc
            steps.text = updatedHabit.steps.toString()
            streak.text = updatedHabit.streak.toString()
            alltime.text = updatedHabit.allTime.toString()
            comp.text = updatedHabit.comp.toString()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.habit_info_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.editMenuOption ){
            editHabit()
        }
        else if(item.itemId == R.id.deleteMenuOption){
            deleteHabit()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun editHabit(){
        val habitFormIntent = Intent(applicationContext, HabitFormActivity::class.java)
        habitFormIntent.putExtra("PARENT_ACTIVITY_NAME", "HABIT_INFO")
        habitFormIntent.putExtra("habit_filled_info", intent.getParcelableExtra<Habit>("habit_info"))
        startActivityForResult(habitFormIntent, 300)
    }

    private fun deleteHabit(){
        val mainActIntent = Intent(applicationContext, MainActivity::class.java)
        mainActIntent.putExtra("del_habit", title)
        setResult(200, mainActIntent)
        finish()
    }

}