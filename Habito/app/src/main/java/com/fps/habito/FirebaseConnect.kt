package com.fps.habito

import android.widget.Toast
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import java.util.HashMap

class FirebaseConnect {

    //Connection initialization
    private val fbdatabase = FirebaseFirestore.getInstance()

    fun sendData(habit: Habit) {

        fbdatabase
            .collection("Habit")
            .document("Suffering")
            .set(habit)
            .addOnSuccessListener {
                print("Send Passed")

            }
            .addOnFailureListener {
                print("Send Failed")
            }

    }

    fun getData(habitName: String) = fbdatabase.collection("Habit").document(habitName).get()

}


