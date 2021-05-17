package com.fps.habito

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.nio.file.Files.delete

class HabitInfo : AppCompatActivity() {

    private val habitName: TextView by lazy { findViewById(R.id.habitName) }
    private val desc: TextView by lazy { findViewById(R.id.desc) }
    private val steps: TextView by lazy { findViewById(R.id.steps) }

    private val streak: TextView by lazy { findViewById(R.id.streakValue) }
    private val alltime: TextView by lazy { findViewById(R.id.alltimeValue) }
    private val comp: TextView by lazy { findViewById(R.id.compValue) }

    private val edit : Button by lazy {findViewById(R.id.edit)}
    private val delete : Button by lazy {findViewById(R.id.delete)}

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_info)

        val habitInfo = intent.getStringArrayListExtra("habit_info")!!

        habitName.text = habitInfo[0]
        desc.text = habitInfo[1]

        steps.text = habitInfo[2]

        streak.text = habitInfo[3]
        alltime.text = habitInfo[4]
        comp.text = habitInfo[5]

        deleteHabit()

    }

    private fun deleteHabit(){

        delete.setOnClickListener {
            val mainActIntent = Intent(applicationContext, MainActivity::class.java)

            mainActIntent.putExtra("del_habit", habitName.text)

            setResult(200, mainActIntent)
            finish()
        }

    }

}