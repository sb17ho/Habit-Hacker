package com.fps.habito

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.GridView


class HabitIconPickerActivity : AppCompatActivity() {

    private val iconsGrid: GridView by lazy { findViewById(R.id.iconsGrid) }

    private val allIcons = arrayListOf(
        R.drawable.barbell,
        R.drawable.bath,
        R.drawable.binary_code,
        R.drawable.book,
        R.drawable.bucket,
        R.drawable.clock,
        R.drawable.drugs,
        R.drawable.eating,
        R.drawable.fitness,
        R.drawable.glass_of_water,
        R.drawable.grooming,
        R.drawable.no_junk_food,
        R.drawable.notebook,
        R.drawable.pet,
        R.drawable.wake_up,
        R.drawable.wardrobe,
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_icon_picker)

        iconsGrid.adapter = IconAdapter(this, allIcons)

        sendSelectedIcon()
    }

    private fun sendSelectedIcon() {

        iconsGrid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val habitFormIntent = Intent(this, HabitFormActivity::class.java)
            habitFormIntent.putExtra("selected_icon", allIcons[position])
            setResult(500, habitFormIntent)
            finish()
        }

    }

}