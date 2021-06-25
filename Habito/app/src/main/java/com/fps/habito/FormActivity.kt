package com.fps.habito

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout
import java.util.*


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

    private val resultContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            when (it.resultCode) {
                500 -> {
                    val iconRes = it.data!!.getIntExtra("selected_icon", 0)
                    icon.setImageResource(iconRes)
                    icon.tag = iconRes
                }
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_form)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.primary_pink)))
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
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
                    habitName.endIconMode = TextInputLayout.END_ICON_NONE
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
        return if (habitName.editText!!.text.isEmpty()) "Habit name is required"
        else null
    }


    private fun updatedHabitListener() {
        done.setOnClickListener {
            val habitInfoIntent = Intent(this, InfoActivity::class.java)

            habit.desc = habitDesc.editText!!.text.toString()
            habit.icon = icon.tag as Int
            habit.progress.steps = steps.editText!!.text.toString().toInt()

            habit.reminder =
                if (reminderSwitch.isChecked) habitReminderFromClock
                else Reminder()

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

        if (habit.reminder.validate() && reminderTextView.visibility == View.GONE)
            reminderTextView.visibility = View.VISIBLE

        reminderTextView.text =
            if (habit.reminder.validate()) {
                reminderSwitch.isChecked = true
                reminderTextView.visibility = View.VISIBLE
                val reminderTime: String =
                    resources.getString(R.string.remindMeAt) + " " + habit.reminder.toString()
                reminderTime
            } else {
                reminderSwitch.isChecked = false
                reminderTextView.visibility = View.GONE
                resources.getString(R.string.remindMeAt)
            }

        habitReminderFromClock = habit.reminder
    }

    private fun selectReminder() {

        val calendar = Calendar.getInstance()

        val reminderPickerDialog = TimePickerDialog(
            this,
            R.style.DialogTheme,
            { _, selectedHour, selectedMinute ->
                habitReminderFromClock = Reminder(
                    kotlin.math.abs(12 - selectedHour),
                    selectedMinute,
                    if (selectedHour < 12) "am" else "pm"
                )
                reminderTextView.text = habitReminderFromClock.toString()
            },
            calendar[Calendar.HOUR_OF_DAY],
            calendar[Calendar.MINUTE],
            false
        )

        reminderSwitch.setOnCheckedChangeListener { _, isChecked ->
            reminderTextView.visibility =
                if (isChecked) {
                    reminderPickerDialog.show()
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }

    }


    private fun selectIcon() {
        icon.setOnClickListener {
            resultContract.launch(Intent(this, IconPickerActivity::class.java))
        }
    }

    private fun createHabitListener() {

        done.setOnClickListener {

            if (habitName.error == null && habitName.editText!!.text.isNotEmpty()) {

                val mainIntent = Intent(applicationContext, MainActivity::class.java)

                mainIntent.putExtra(
                    "new_habit",
                    Habit(
                        name = habitName.editText!!.text.toString(),

                        desc =
                        if (habitDesc.editText!!.text.toString().isEmpty()) ""
                        else habitDesc.editText!!.text.toString(),

                        icon =
                        if (icon.tag == null) R.drawable.nil
                        else icon.tag.toString().toInt(),

                        progress = Progress(
                            0,
                            if (steps.editText!!.text.toString().isEmpty()) 1
                            else steps.editText!!.text.toString().toInt(),
                            Status.NOT_STARTED.toString()
                        ),

                        reminder = habitReminderFromClock,

                        stats = Stats(startDate = Calendar.getInstance().time)
                    )
                )
                setResult(100, mainIntent)
                finish()
            } else {
                habitName.error = errorMessage()
            }

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}