package com.fps.habito

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_layout)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /*TODO: reminder setting
    *  About: Software info and developers info
    *   How to use: How to add, remove (basic stuff)
    * */
}