package com.fps.habito

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.GridLayout
import android.widget.GridView
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val habitsGrid: GridView by lazy { findViewById(R.id.habitsGrid) }
    private val add: Button by lazy { findViewById(R.id.add) }
    private val remove: Button by lazy { findViewById(R.id.remove) }

    private var habits: ArrayList<String> = mutableListOf("new") as ArrayList<String>
    var h = "empty"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        add.setOnClickListener {
            val habitInfoIntent = Intent(this, HabitInfo::class.java)
            startActivity(habitInfoIntent)
        }



    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("all", habits)
    }

    override fun onResume() {
        super.onResume()
        habits.add(intent.getStringExtra("new_habit").toString())
        println(habits)
    }









}

