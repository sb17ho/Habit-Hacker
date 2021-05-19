package com.fps.habito

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout


class HabitFormActivity : AppCompatActivity() {

    private val icons: GridView by lazy { findViewById(R.id.icons) }

    private val habitName: TextInputLayout by lazy { findViewById(R.id.habitName) }
    private val habitDesc: TextInputLayout by lazy { findViewById(R.id.habit_desc) }

    private val steps: TextInputLayout by lazy { findViewById(R.id.steps) }

    private val done: Button by lazy { findViewById(R.id.done) }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_form)

        icons.adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, listOf("icons", "icons", "icons", "icons", "icons", "icons"))

        if (intent.getStringExtra("PARENT_ACTIVITY_NAME").equals("MAIN")) {
            sendNewHabitData()
        } else if (intent.getStringExtra("PARENT_ACTIVITY_NAME").equals("HABIT_INFO")) {

            println("THIS IS COOL")

            // fills HabitForm with data received from HabitInfo
            fillWithHabitData()

            done.setOnClickListener {
                val habitInfoIntent = Intent(this, HabitInfoActivity::class.java)

                habitInfoIntent.putStringArrayListExtra("updated_habit",
                        arrayListOf(
                                habitName.editText!!.text.toString(),
                                habitDesc.editText!!.text.toString(),
                                steps.editText!!.text.toString(),
                        ))

                setResult(300, habitInfoIntent)
                finish()
            }
        }


    }

    private fun fillWithHabitData() {

        val habit_filled_info = intent.getStringArrayListExtra("habit_filled_info")

        println("filled habit info $habit_filled_info")
        habitName.editText!!.setText(habit_filled_info?.get(0))
        habitDesc.editText!!.setText(habit_filled_info?.get(1))
        steps.editText!!.setText(habit_filled_info?.get(2))

    }

    /**
     * Send the newly created habit to the main activity.
     */
    private fun sendNewHabitData() {

        done.setOnClickListener {

            val mainActIntent = Intent(applicationContext, MainActivity::class.java)

            mainActIntent.putStringArrayListExtra(
                    "new_habit",
                    arrayListOf(
                            habitName.editText!!.text.toString(),
                            if (habitDesc.editText!!.text.toString().isEmpty()) "" else habitDesc.editText!!.text.toString(),
                            if (steps.editText!!.text.toString().isEmpty()) "1" else steps.editText!!.text.toString(),
                    )
            )

            if (TextUtils.isEmpty(habitName.editText!!.text)) {
                habitName.error = "Habit name is required"
            } else {
                setResult(100, mainActIntent)
                finish()
            }

        }

    }

}