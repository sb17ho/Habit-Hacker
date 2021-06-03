package com.fps.habito

import com.google.firebase.firestore.FirebaseFirestore


class FirebaseConnection {

    val firebaseDatabase = FirebaseFirestore.getInstance()

    fun sendData(habit: Habit) {

        firebaseDatabase
            .collection("Habit")
            .document("Suffering")
            .set(habit)
            .addOnSuccessListener { println("Send/Edit Successed") }
            .addOnFailureListener { print("Send/Edit Failed") }

    }

    /*
    * Returns DocumentSnap of the habit using habit name
    * @param takes in habit name
    * @return Task<DocumentSnapshot>
    * */
    fun getData(habitName: String) = firebaseDatabase.collection("Habit").document(habitName).get()


}


