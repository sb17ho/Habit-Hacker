package com.fps.habito

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
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

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_form)

        when (intent.getStringExtra("PARENT_ACTIVITY_NAME")) {
            "MAIN" -> {
                selectIcon()
                selectReminder()
                createHabit()
            }
            "HABIT_INFO" -> {
                fillViews()
                selectReminder()
                selectIcon()
                doneButtonOnClickListener()
            }
        }

    }

    private fun doneButtonOnClickListener() {

        done.setOnClickListener {

            val habitInfoIntent = Intent(this, InfoActivity::class.java)

            MainActivity.firestoreCollectionReference
                .document(intent.getStringExtra("edit_habit")!!)
                .set(createHabitFromActivity())

            habitInfoIntent.putExtra("updated_habit", habitName.editText!!.text.toString())
            setResult(300, habitInfoIntent)
            finish()
        }

    }

    private fun fillViews() {

        MainActivity.firestoreCollectionReference
            .document(intent.getStringExtra("edit_habit")!!)
            .get()
            .addOnSuccessListener {

                val currentHabit = it.toObject(Habit::class.java)!!

                habitName.editText!!.setText(currentHabit.name)
                habitDesc.editText!!.setText(currentHabit.desc)

                icon.setImageResource(currentHabit.icon)
                icon.tag = currentHabit.icon

                steps.editText!!.setText(currentHabit.progress.steps.toString())

                reminderTextView.text = if (currentHabit.reminder.isSet()) {
                    reminderSwitch.isChecked = true
                    currentHabit.reminder.toString()
                } else {
                    reminderSwitch.isChecked = false
                    "Tap to set reminder"
                }

                habitReminderFromClock = currentHabit.reminder

            }

    }

    private fun selectIcon() {

        icon.setOnClickListener {
            startActivityForResult(Intent(this, IconPickerActivity::class.java), 1)
        }

    }

    private fun selectReminder() {

        reminderTextView.setOnClickListener {
            if (reminderSwitch.isChecked) {
                selectReminderFromClock()
            }
        }

    }

    private fun selectReminderFromClock() {

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

    private fun createHabitFromActivity(): Habit {

        val newHabit = Habit(habitName.editText!!.text.toString())

        newHabit.desc =
            if (habitDesc.editText!!.text.toString().isEmpty()) ""
            else habitDesc.editText!!.text.toString()

        newHabit.icon =
            if (icon.tag == null) R.drawable.nil
            else icon.tag.toString().toInt()

        newHabit.progress.steps =
            if (steps.editText!!.text.toString().isEmpty()) 1
            else steps.editText!!.text.toString().toInt()

        newHabit.stats.startDate = Calendar.getInstance().time

        newHabit.reminder = habitReminderFromClock

        return newHabit

    }

    private fun createHabit() {

        done.setOnClickListener {

            val newHabit = createHabitFromActivity()

            val mainIntent = Intent(applicationContext, MainActivity::class.java)
            mainIntent.putExtra("new_habit_name", newHabit.name)

            if (TextUtils.isEmpty(habitName.editText!!.text)) {
                habitName.error = "Habit name is required"
            } else {
                MainActivity.firestoreCollectionReference
                    .document(newHabit.name)
                    .set(newHabit)
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
        outState.putParcelable("filled_habit", createHabitFromActivity())

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {

        val restoredHabit: Habit = savedInstanceState.getParcelable("filled_habit")!!
        icon.setImageResource(restoredHabit.icon)
        icon.tag = restoredHabit.icon
        habitName.editText!!.setText(restoredHabit.name)
        habitDesc.editText!!.setText(restoredHabit.desc)
        steps.editText!!.setText(restoredHabit.progress.steps.toString())

    }

}