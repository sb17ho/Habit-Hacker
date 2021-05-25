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

    //Connection initialization
    var fbdatabase = FirebaseFirestore.getInstance()

    //data class to transport Habit data between firebase and app
    data class FirebaseModel(
            var daysCounts:Long,
            var description: String,
            var hid: String,
            var progress: Long,
            var status: String,
            var steps: Long,
            var streak: Long,
            var uid: String
    )

    /*
    * Packs up an objects with all parameters and Sends data to firebase.
    *
    * @params All habit elements
    * (i.e.  daysCount, description, hid, progress, status, steps, streak, uid)
    *
    * @return boolean of transaction status
    * */
     fun sendorEditData(
             daysCount:Int ,
             description: String ,
             hid: String ,
             progress: Int ,
             status: String ,
             steps: Int ,
             streak: Int ,
             uid: String
     ): Boolean {


        var habit : HashMap<String, Any> = HashMap<String, Any> ()
        habit.put("daysCounts", daysCount)
        habit.put("description", description)
        habit.put("hid", hid)
        habit.put("progress", progress)
        habit.put("status", status)
        habit.put("steps", steps)
        habit.put("streak", streak)
        habit.put("uid", uid)

         var result:Boolean  = false


        fbdatabase.collection("Habit").document("Suffering")
                .set(habit)
                .addOnSuccessListener {
                    print("habit chali gayi!")
                    result = true
                }
                .addOnFailureListener {
                    print("habit nhi gayi!")
                    result = false
                }

         return result

    }


     /*
     * Gets data from firebase using habit name and
     *          returns a data class with all received data.
     *
     * @param takes in habit name
     * @return data class with all the elments of Habit object
     * */
     fun getData(habitname: String): FirebaseModel {


         var data = FirebaseModel(0,"","",0,"",0,0,"")

         var habit : DocumentSnapshot?

        fbdatabase.collection("Habit").document(habitname).get()
                .addOnCompleteListener{
                    task ->
                    if (task.isSuccessful) {
                        habit  = task.result

                        data.daysCounts = habit!!.get("daysCounts") as Long
                        data.description = habit!!.get("description") as String
                        data.hid = habit!!.get("hid") as String
                        data.progress = habit!!.get("progress") as Long
                        data.status = habit!!.get("status") as String
                        data.steps = habit!!.get("steps") as Long
                        data.streak = habit!!.get("streak") as Long
                        data.uid = habit!!.get("uid") as String


                    } else {
                        print("Sigh!, nothing reached!")
                    }
                }

        return data
    }


}


