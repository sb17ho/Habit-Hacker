package com.fps.habito

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import java.lang.Math.abs
import java.time.LocalDate
import java.util.*

class HabitFormActivity : AppCompatActivity() {

    private val icon: ImageView by lazy { findViewById(R.id.icon) }
    private val habitName: TextInputLayout by lazy { findViewById(R.id.habitName) }
    private val habitDesc: TextInputLayout by lazy { findViewById(R.id.habit_desc) }
    private val steps: TextInputLayout by lazy { findViewById(R.id.steps) }
    private val done: ImageView by lazy { findViewById(R.id.done) }

    private val reminderTextView: TextView by lazy { findViewById(R.id.reminderTextView) }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_form)


        if (intent.getStringExtra("PARENT_ACTIVITY_NAME").equals("MAIN")) {
            selectHabitIcon()
            sendNewHabitData()

            getReminderTime()


        } else if (intent.getStringExtra("PARENT_ACTIVITY_NAME").equals("HABIT_INFO")) {

            fillWithHabitData()
            selectHabitIcon()


            done.setOnClickListener {
                val habitInfoIntent = Intent(this, HabitInfoActivity::class.java)

                habitInfoIntent.putExtra("updated_habit",
                        Habit(
                                icon.tag as Int,
                                habitName.editText!!.text.toString(),
                                habitDesc.editText!!.text.toString(),
                                steps.editText!!.text.toString().toInt(),
                                0,
                                0.0,
                                0,
                        )
                )
                setResult(300, habitInfoIntent)
                finish()
            }
        }

    }


    private fun fillWithHabitData() {

        val habit_filled = intent.getParcelableExtra<Habit>("habit_filled_info")!!
        icon.setImageResource(habit_filled.icon)
        icon.tag = habit_filled.icon
        habitName.editText!!.setText(habit_filled.name)
        habitDesc.editText!!.setText(habit_filled.desc)
        steps.editText!!.setText(habit_filled.steps.toString())


    }

    private fun getReminderTime() {

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val min = calendar.get(Calendar.MINUTE)

        reminderTextView.setOnClickListener {

            TimePickerDialog(this, { view, hourOfDay, minute ->

                val reminderHour = "${abs(12 - hourOfDay)}:"
                val reminderMinute = "$minute "
                val ampm = if (hourOfDay < 12) "am" else "pm"

                reminderTextView.text = reminderHour + reminderMinute + ampm

            }, hour, min, false).show()


        }

    }


    private fun selectHabitIcon() {

        icon.setOnClickListener {
            startActivityForResult(Intent(this, HabitIconPickerActivity::class.java), 1)
        }

    }

    /**
     * Send the newly created habit to the main activity.
     */
    private fun sendNewHabitData() {

        done.setOnClickListener {

            val mainIntent = Intent(applicationContext, MainActivity::class.java)

            mainIntent.putExtra(
                    "new_habit",
                    Habit(
                            if (icon.tag == null) R.drawable.close else icon.tag.toString().toInt(),
                            habitName.editText!!.text.toString(),
                            if (habitDesc.editText!!.text.toString().isEmpty()) "" else habitDesc.editText!!.text.toString(),
                            if (steps.editText!!.text.toString().isEmpty()) 1 else steps.editText!!.text.toString().toInt(),
                            0,
                            0.0,
                            0,
                    )
            )

            if (TextUtils.isEmpty(habitName.editText!!.text)) {
                habitName.error = "Habit name is required"
            } else {
                setResult(100, mainIntent)
                finish()
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 500) {
            val iconRes = data!!.getIntExtra("selected_icon", 0)
            icon.setImageResource(iconRes)
            icon.tag = iconRes
        }

    }


}