package com.fps.habito

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bootup_screen)

        mAuth = FirebaseAuth.getInstance()
        val user_auth = mAuth.currentUser

        Handler().postDelayed({
            if (user_auth != null) {
                val test_Screen = Intent(this, Habit_Main_Screen::class.java)
                startActivity(test_Screen)
                finish() //To avoid going back to main activity
            } else {
                val sign_in_screen = Intent(this, GoogleSignIn::class.java)
                startActivity(sign_in_screen)
                finish() //To avoid going back to main activity
            }
        }, 2000)

    }
}