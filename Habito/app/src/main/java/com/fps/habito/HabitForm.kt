package com.fps.habito

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import java.util.*

class HabitForm : AppCompatActivity() {

    private val done: Button by lazy { findViewById(R.id.delete) }
    private val habitName: EditText by lazy { findViewById(R.id.habitName) }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_form)

        // sends habit creation data back to the main screen
        done.setOnClickListener {
            val mainActIntent = Intent(applicationContext, MainActivity::class.java)
            mainActIntent.putExtra("new_habit_name", habitName.text.toString())
            setResult(RESULT_OK, mainActIntent)
            finish()
        }

    }

}