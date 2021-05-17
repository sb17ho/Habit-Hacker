package com.fps.habito

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.max


class HabitForm : AppCompatActivity() {

    private val icons: GridView by lazy { findViewById(R.id.icons) }

    private val habitName: TextInputLayout by lazy { findViewById(R.id.habitName) }
    private val habitDesc: TextInputLayout by lazy { findViewById(R.id.habit_desc) }

    private val steps: TextInputLayout by lazy { findViewById(R.id.steps) }

    private val done: Button by lazy { findViewById(R.id.done) }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_form)

        steps.editText!!.setText("1")

        val iconsList = listOf("icons", "icons", "icons", "icons", "icons", "icons")
        val iconsAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_expandable_list_item_1,
            iconsList
        )
        icons.adapter = iconsAdapter

        

//        habitName.editText!!.addTextChangedListener {
//            habitDesc.editText!!.requestFocus()
//        }

        getFilledHabitData()

    }

    private fun getFilledHabitData(){
        done.setOnClickListener {

            val mainActIntent = Intent(applicationContext, MainActivity::class.java)

            habitDesc.editText!!.setText("")
            steps.editText!!.setText("1")

            mainActIntent.putStringArrayListExtra(
                "new_habit",
                arrayListOf(
                    habitName.editText!!.text.toString(),
                    habitDesc.editText!!.text.toString(),
                    steps.editText!!.text.toString(),
                )
            )

            if (TextUtils.isEmpty(habitName.editText!!.text)) {
                habitName.error = "Habit name is required"
            } else {
                setResult(RESULT_OK, mainActIntent)
                finish()
            }

        }


    }


}