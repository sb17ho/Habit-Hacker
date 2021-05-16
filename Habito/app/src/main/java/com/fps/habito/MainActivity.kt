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
import kotlin.collections.HashSet

/**
 * TODO figure out a way to pass Set to ArrayAdapter
 */

class MainActivity : AppCompatActivity() {

    private val habitsGrid: GridView by lazy { findViewById(R.id.habitsGrid) }
    private val add: Button by lazy { findViewById(R.id.add) }
    private val remove: Button by lazy { findViewById(R.id.remove) }

    private var habits = HashSet<String>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        add.setOnClickListener {
            val habitInfoIntent = Intent(this, HabitInfo::class.java)
            val result = 1
            startActivityForResult(habitInfoIntent, result)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        habits.add(data?.getStringExtra("new_habit").toString())
        habitsGrid.adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, habits.toArray())
    }

}

