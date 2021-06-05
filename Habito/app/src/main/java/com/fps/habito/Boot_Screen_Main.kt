package com.fps.habito

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class Boot_Screen_Main : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bootup_screen)

        mAuth = FirebaseAuth.getInstance()
        val user_auth = mAuth.currentUser

        Handler().postDelayed({
            if (user_auth != null) {
                val habitMainScreen = Intent(this, MainActivity::class.java)
                habitMainScreen.putExtra("UserName", user_auth.displayName)
                habitMainScreen.putExtra("UserEmail", user_auth.email)
                habitMainScreen.putExtra("UserPhoto", user_auth.photoUrl)
                startActivity(habitMainScreen)
                finish() //To avoid going back to main activity
            } else {
                val sign_in_screen = Intent(this, GoogleSignIn::class.java)
                startActivity(sign_in_screen)
                finish() //To avoid going back to main activity
            }
        }, 2000)

    }
}