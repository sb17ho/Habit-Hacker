package com.fps.habito

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class BootScreenMain : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bootup_screen)

        mAuth = FirebaseAuth.getInstance()
        val userAuth = mAuth.currentUser

        Handler().postDelayed({
            if (userAuth != null) {
                val habitMainScreen = Intent(this, MainActivity::class.java)
                habitMainScreen.putExtra("UserName", userAuth.displayName)
                habitMainScreen.putExtra("UserEmail", userAuth.email)
                habitMainScreen.putExtra("UserPhoto", userAuth.photoUrl)
                startActivity(habitMainScreen)
                finish() //To avoid going back to main activity
            } else {
                val signInScreen = Intent(this, GoogleSignIn::class.java)
                startActivity(signInScreen)
                finish() //To avoid going back to main activity
            }
        }, 2000)

    }
}