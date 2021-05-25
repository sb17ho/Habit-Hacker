package com.fps.habito

import android.widget.Toast
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import java.util.HashMap

class firebaseConnect {

    var fbdatabase = FirebaseFirestore.getInstance()


     fun sendorEditData(
             daysCount:Int ,
             description: String ,
             hid: String ,
             progress: Int ,
             status: String ,
             steps: Int ,
             streak: Int ,
             uid: String
     ){


        var habit : HashMap<String, Any> = HashMap<String, Any> ()
        habit.put("daysCounts", daysCount)
        habit.put("description", description)
        habit.put("hid", hid)
        habit.put("progress", progress)
        habit.put("status", status)
        habit.put("steps", steps)
        habit.put("streak", streak)
        habit.put("uid", uid)



        fbdatabase.collection("Habit").document("Suffering")
                .set(habit)
                .addOnSuccessListener {
                    print("habit chali gayi!")
                }
                .addOnFailureListener {
                    print("habit nhi gayi!")
                }

    }

    data class FirebaseModel(
        val daysCount:Int,
        val description: String,
        val hid: String,
        val progress: Int,
        val status: String,
        val steps: Int,
        val streak: Int,
        val uid: String
    )



     fun getData(){




         var data : FirebaseModel

         var habit : DocumentSnapshot?

        fbdatabase.collection("Habit").document("Suffering").get()
                .addOnCompleteListener{
                    task ->
                    if (task.isSuccessful) {
                        habit  = task.result
//json data collection here--------------------------

//                        val daysCount =
//                        val description =
//                        val hid =
//                        val progress =
//                        val status: String,
//                        val steps: Int,
//                        val streak: Int,
//                        val uid: String




                    } else {
                        print("Sigh!, nothing reached!")
                    }
                }

    }


}


