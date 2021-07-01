package com.fps.habito

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class HowToUse : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.how_to_use)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}