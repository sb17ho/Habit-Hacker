package com.fps.habito

import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout
import java.util.*

/**
 * TODO add units for steps for land and portrait
 */
class FormActivity : AppCompatActivity() {

    private val icon: ImageView by lazy { findViewById(R.id.icon) }
    private val habitName: TextInputLayout by lazy { findViewById(R.id.habitName) }
    private val habitDesc: TextInputLayout by lazy { findViewById(R.id.habit_desc) }
    private val steps: TextInputLayout by lazy { findViewById(R.id.steps) }
    private val reminderSwitch: SwitchCompat by lazy { findViewById(R.id.reminderSwitch) }
    private val reminderTextView: TextView by lazy { findViewById(R.id.reminderTextView) }
    private val done: ImageView by lazy { findViewById(R.id.done) }

    private var habitReminderFromClock = Reminder()

    private lateinit var habit: Habit

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_form)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.primary_pink)))
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.statusBarColor = resources.getColor(R.color.primary_pink)


        when (intent.getStringExtra("PARENT_ACTIVITY_NAME")) {
            "MAIN" -> {
                title = "Create new habit"
                selectIcon()
                selectReminder()

                habitName.editText!!.addTextChangedListener {
                    habitName.error = errorMessage()
                }

                createHabitListener()
            }
            "HABIT_INFO" -> {

                title = "Edit habit"

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    habitName.editText!!.focusable = View.NOT_FOCUSABLE
                }

                habit = intent.getParcelableExtra("habit_filled_info")!!

                fillViews()
                selectIcon()
                selectReminder()
                updatedHabitListener()


            }

        }

    }

    private fun errorMessage(): String? {
        return if (habitName.editText!!.text.isEmpty()) {
            "Habit name is required"
        } else {
            null
        }
    }

    

    private fun updatedHabitListener() {
        done.setOnClickListener {
            val habitInfoIntent = Intent(this, InfoActivity::class.java)

            habit.desc = habitDesc.editText!!.text.toString()
            habit.icon = icon.tag as Int
            habit.progress.steps = steps.editText!!.text.toString().toInt()
            habit.reminder = habitReminderFromClock

            habitInfoIntent.putExtra("updated_habit", habit)
            setResult(100, habitInfoIntent)
            finish()
        }
    }

    private fun fillViews() {

        icon.setImageResource(habit.icon)
        icon.tag = habit.icon
        habitName.editText!!.setText(habit.name)
        habitDesc.editText!!.setText(habit.desc)
        steps.editText!!.setText(habit.progress.steps.toString())

        reminderTextView.text = if (habit.reminder.validate()) {
            reminderSwitch.isChecked = true
            habit.reminder.toString()
        } else {
            reminderSwitch.isChecked = false
            "Tap to set reminder"
        }

        habitReminderFromClock = habit.reminder

    }

    private fun selectReminder() {

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
                Reminder(
                    kotlin.math.abs(12 - hourOfDay),
                    minute,
                    if (hourOfDay < 12) "am" else "pm"
                )
            reminderTextView.text = habitReminderFromClock.toString()
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()

    }

    private fun selectIcon() {

        icon.setOnClickListener {
            startActivityForResult(Intent(this, IconPickerActivity::class.java), 1)
        }

    }

    private fun createHabitListener() {

        done.setOnClickListener {

           // println("this is not cool ${habitName.error}")

            if (habitName.error == null && habitName.editText!!.text.isNotEmpty()) {

                val mainIntent = Intent(applicationContext, MainActivity::class.java)

                habit = Habit(
                    name = habitName.editText!!.text.toString(),
                    stats = Stats(startDate = Calendar.getInstance().time)
                )

                habit.desc =
                    if (habitDesc.editText!!.text.toString().isEmpty()) ""
                    else habitDesc.editText!!.text.toString()

                habit.icon =
                    if (icon.tag == null) R.drawable.nil
                    else icon.tag.toString().toInt()

                habit.progress.steps =
                    if (steps.editText!!.text.toString().isEmpty()) 1
                    else steps.editText!!.text.toString().toInt()

                habit.reminder = habitReminderFromClock

                mainIntent.putExtra("new_habit", habit)
                setResult(100, mainIntent)
                finish()
            }
            else{
                habitName.error = errorMessage()
            }


//            if (TextUtils.isEmpty(habitName.editText!!.text)) {
//                habitName.error = "Habit name is required"
//            } else {
//                setResult(100, mainIntent)
//                finish()
//            }

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        habit = Habit(
            name = habitName.editText!!.text.toString(),
            stats = Stats(startDate = Calendar.getInstance().time)
        )

        habit.desc = if (habitDesc.editText!!.text.toString().isEmpty()) ""
        else habitDesc.editText!!.text.toString()

        habit.icon =
            if (icon.tag == null) R.drawable.nil
            else icon.tag.toString().toInt()

        habit.progress.steps =
            if (steps.editText!!.text.toString().isEmpty()) 1
            else steps.editText!!.text.toString().toInt()
        habit.reminder = habitReminderFromClock

        outState.putParcelable("instance_state", habit)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {

        val instanceStateHabit = savedInstanceState.getParcelable<Habit>("instance_state")!!

        icon.setImageResource(instanceStateHabit.icon)
        icon.tag = instanceStateHabit.icon
        habitName.editText!!.setText(instanceStateHabit.name)
        habitDesc.editText!!.setText(instanceStateHabit.desc)
        steps.editText!!.setText(instanceStateHabit.progress.steps.toString())
    }

}