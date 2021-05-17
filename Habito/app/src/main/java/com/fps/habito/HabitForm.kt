package com.fps.habito

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.max


class HabitForm : AppCompatActivity() {

    private val icons: GridView by lazy { findViewById(R.id.icons) }

    private val habitName: EditText by lazy { findViewById(R.id.habitName) }
    private val habitDesc: EditText by lazy { findViewById(R.id.habit_desc) }

    private val steps: EditText by lazy { findViewById(R.id.steps) }
    private val increment: Button by lazy { findViewById(R.id.increment) }
    private val decrement: Button by lazy { findViewById(R.id.decrement) }

    private val reminder: EditText by lazy { findViewById(R.id.reminder) }
    private val reminderSwitch: Switch by lazy { findViewById(R.id.reminderSwitch) }

    private val done: Button by lazy { findViewById(R.id.delete) }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_form)

        steps.setText("1")

        val iconsList = listOf("icons", "icons", "icons", "icons", "icons", "icons")
        val iconsAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_expandable_list_item_1,
            iconsList
        )
        icons.adapter = iconsAdapter


        // sends habit creation data back to the main screen
        done.setOnClickListener {
            val mainActIntent = Intent(applicationContext, MainActivity::class.java)

            habitDesc.setText("")
            steps.setText("1")
            reminder.setText("")

            mainActIntent.putStringArrayListExtra(
                "new_habit",
                arrayListOf(
                    habitName.text.toString(),
                    habitDesc.text.toString(),
                    steps.text.toString(),
                    reminder.text.toString()
                )
            )

            if (TextUtils.isEmpty(habitName.text)) {
                habitName.error = "Habit name is required"
            } else {
                setResult(RESULT_OK, mainActIntent)
                finish()
            }

        }


        stepsButtonControl()

        toggleReminder()

    }

    private fun stepsButtonControl() {

        increment.setOnClickListener {
            steps.setText((steps.text.toString().toInt() + 1).toString())
        }

        decrement.setOnClickListener {
            steps.setText(max(steps.text.toString().toInt() - 1, 1).toString())
        }

    }

    private fun toggleReminder() {

        reminderSwitch.setOnClickListener {
            reminder.isEnabled = reminderSwitch.isChecked
        }

    }

}