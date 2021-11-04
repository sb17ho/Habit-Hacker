package com.fps.habito

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleSignIn : AppCompatActivity() {
    companion object {
        private const val RC_SIGN_IN = 120
    }

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    private val registerForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (it.resultCode == RESULT_OK) {
                val task =
                    com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(
                        it.data
                    )
                val exception: Exception? = task.exception
                if (task.isSuccessful) {
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        val account = task.getResult(ApiException::class.java)!!
//                    Log.d("SignInActivity", "firebaseAuthWithGoogle:" + account.id)
                        firebaseAuthWithGoogle(account.idToken!!)
                    } catch (e: ApiException) {
                        // Google Sign In failed, update UI appropriately
                        Log.w("SignInActivity", "Google sign in failed", e)
                    }
                } else {
                    Log.w("SignInActivity", exception.toString())
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)
        googleSignIn()
    }

    private fun googleSignIn() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient =
            com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(this, gso)

        //Firebase Authentication Instance
        mAuth = FirebaseAuth.getInstance()

        val signInButton: SignInButton = findViewById(R.id.google_sign_in_button)
        signInButton.setOnClickListener {
            signIn() //Ask user to sign in with his/her google account
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent //Callback from onActivityResult method
        registerForResult.launch(signInIntent) //Check if our user signed in successfully
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intentTo = Intent(this, MainActivity::class.java)
                    intentTo.putExtra("UserName", mAuth.currentUser?.displayName)
                    intentTo.putExtra("UserEmail", mAuth.currentUser?.email)
                    intentTo.putExtra("UserPhoto", mAuth.currentUser?.photoUrl)
                    startActivity(intentTo)
                    finish() //To avoid going back to boot screen
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SignInActivity", "signInWithCredential:failure", task.exception)
                }
            }
    }
}