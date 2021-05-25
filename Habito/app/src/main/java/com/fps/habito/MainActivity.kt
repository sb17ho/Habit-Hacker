package com.fps.habito

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Time
import java.util.HashMap

/**
 * TODO figure out a way to pass Set to ArrayAdapter
 */

class MainActivity : AppCompatActivity() {

    private val habitsGrid: GridView by lazy { findViewById(R.id.habitsGrid) }
    private val add: Button by lazy { findViewById(R.id.add) }

    private var habits = ArrayList<Habit>()

    private val firebaseAccess = firebaseConnect()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        firebaseAccess.sendorEditData(7,"sadfasdfa","sadfa",5,"dfgdfg",8,0,"fgdfg")


        firebaseAccess.getData("Suffering")


        // Starts HabitForm activity
        add.setOnClickListener {
            val habitFormIntent = Intent(this, HabitFormActivity::class.java)
            habitFormIntent.putExtra("PARENT_ACTIVITY_NAME", "MAIN")
            val result = 1
            startActivityForResult(habitFormIntent, result)
        }

        openHabitInfo()
    }







    /**
     * Starts HabitInfoActivity
     */
    private fun openHabitInfo() {

        habitsGrid.onItemLongClickListener = OnItemLongClickListener { a, b, c, d ->
            val habitInfoIntent = Intent(this, HabitInfoActivity::class.java)

            habitInfoIntent.putExtra("PARENT_ACTIVITY_NAME", "MAIN")
            habitInfoIntent.putStringArrayListExtra("habit_info",
                    arrayListOf(
                            habits[c].name,
                            habits[c].desc,
                            habits[c].steps.toString(),
                            habits[c].streak.toString(),
                            habits[c].allTime.toString(),
                            habits[c].comp.toString(),
                    ))
            val result = 2
            startActivityForResult(habitInfoIntent, result)
            true
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        println("RESULT $resultCode")

        if (resultCode == 100) { // addition
            val newHabitData = data?.getStringArrayListExtra("new_habit")!!

            val newHabit = Habit(
                    newHabitData[0],
                    newHabitData[1],
            )

            if (!habits.contains(newHabit)) {
                habits.add(newHabit)
                habitsGrid.adapter = HabitAdapter(this, habits)
            }

        } else if (resultCode == 200) { // deletion
            habits.removeIf { it.name == data!!.getStringExtra("del_habit") }
            habitsGrid.adapter = HabitAdapter(this, habits)
        } else if (resultCode == 400) { // returning to main with no changes

            val updatedHabit = data!!.getStringArrayListExtra("habit_for_main")

            val targetHabit = habits.find { it.name == updatedHabit?.get(0) }
            targetHabit?.name = updatedHabit?.get(1)!!
            targetHabit?.desc = updatedHabit[2]
            targetHabit?.steps = updatedHabit[3]!!.toInt()

            habitsGrid.adapter = HabitAdapter(this, habits)

        }

    }

}

