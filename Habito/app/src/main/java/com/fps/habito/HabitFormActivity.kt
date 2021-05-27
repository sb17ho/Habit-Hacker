package com.fps.habito

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.textfield.TextInputLayout
import java.util.*

/**
 * TODO add units for steps for land and portrait
 */
class HabitFormActivity : AppCompatActivity() {

    private val icon: ImageView by lazy { findViewById(R.id.icon) }
    private val habitName: TextInputLayout by lazy { findViewById(R.id.habitName) }
    private val habitDesc: TextInputLayout by lazy { findViewById(R.id.habit_desc) }
    private val steps: TextInputLayout by lazy { findViewById(R.id.steps) }
    private val reminderSwitch: SwitchCompat by lazy { findViewById(R.id.reminderSwitch) }
    private val reminderTextView: TextView by lazy { findViewById(R.id.reminderTextView) }
    private val done: ImageView by lazy { findViewById(R.id.done) }

    private var habitReminderFromClock = HabitReminder()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_form)

        if (intent.getStringExtra("PARENT_ACTIVITY_NAME").equals("MAIN")) {
            selectHabitIcon()
            getReminderTime()
            sendNewHabitData()
        } else if (intent.getStringExtra("PARENT_ACTIVITY_NAME").equals("HABIT_INFO")) {
            fillWithHabitData()
            getReminderTime()
            selectHabitIcon()

            done.setOnClickListener {
                val habitInfoIntent = Intent(this, HabitInfoActivity::class.java)

                habitInfoIntent.putExtra(
                        "updated_habit",
                        Habit(
                                icon.tag as Int,
                                habitName.editText!!.text.toString(),
                                habitDesc.editText!!.text.toString(),
                                steps.editText!!.text.toString().toInt(),
                                0,
                                0.0,
                                0,
                                habitReminderFromClock
                        )
                )
                setResult(300, habitInfoIntent)
                finish()
            }
        }

    }

    private fun fillWithHabitData() {

        val habitFilled = intent.getParcelableExtra<Habit>("habit_filled_info")!!

        icon.setImageResource(habitFilled.icon)
        icon.tag = habitFilled.icon
        habitName.editText!!.setText(habitFilled.name)
        habitDesc.editText!!.setText(habitFilled.desc)
        steps.editText!!.setText(habitFilled.steps.toString())

        reminderTextView.text = if (habitFilled.habitReminder.isSet()) {
            reminderSwitch.isChecked = true
            habitFilled.habitReminder.toString()
        } else {
            reminderSwitch.isChecked = false
            "Tap to set reminder"
        }

        habitReminderFromClock = habitFilled.habitReminder

    }


    private fun getReminderTime() {

        reminderTextView.setOnClickListener {
            if (reminderSwitch.isChecked) {
                setHabitReminderFromClock()
            }
        }

    }

    private fun setHabitReminderFromClock() {

        val calendar = Calendar.getInstance()

        TimePickerDialog(this, { view, hourOfDay, minute ->
            habitReminderFromClock =
                    HabitReminder(
                            kotlin.math.abs(12 - hourOfDay),
                            minute,
                            if (hourOfDay < 12) "am" else "pm"
                    )
            reminderTextView.text = habitReminderFromClock.toString()
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()

    }

    private fun selectHabitIcon() {

        icon.setOnClickListener {
            startActivityForResult(Intent(this, HabitIconPickerActivity::class.java), 1)
        }

    }


    private fun sendNewHabitData() {

        done.setOnClickListener {

            val mainIntent = Intent(applicationContext, MainActivity::class.java)

            val newHabit = Habit(
                    if (icon.tag == null) R.drawable.nil else icon.tag.toString().toInt(),
                    habitName.editText!!.text.toString(),
                    if (habitDesc.editText!!.text.toString().isEmpty()) "" else habitDesc.editText!!.text.toString(),
                    if (steps.editText!!.text.toString().isEmpty()) 1 else steps.editText!!.text.toString().toInt(),
                    0,
                    0.0,
                    0,
                    habitReminderFromClock
            )

            newHabit.startDate = Calendar.getInstance().time

            mainIntent.putExtra("new_habit", newHabit)



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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(
                "FILLED_HABIT",
                Habit(
                        if (icon.tag == null) R.drawable.nil else icon.tag.toString().toInt(),
                        habitName.editText!!.text.toString(),
                        habitDesc.editText!!.text.toString(),
                        if (steps.editText!!.text.toString() == "") 1
                        else steps.editText!!.text.toString().toInt(),
                        0,
                        0.0,
                        0,
                        habitReminderFromClock
                )
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val restoredHabit: Habit = savedInstanceState.getParcelable("FILLED_HABIT")!!
        icon.setImageResource(restoredHabit.icon)
        icon.tag = restoredHabit.icon
        habitName.editText!!.setText(restoredHabit.name)
        habitDesc.editText!!.setText(restoredHabit.desc)
        steps.editText!!.setText(restoredHabit.steps.toString())
    }

}