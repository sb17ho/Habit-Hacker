package com.fps.habito

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.GridView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private val habitsGrid: GridView by lazy { findViewById(R.id.habitsGrid) }
    private val add: ImageView by lazy { findViewById(R.id.add) }

    companion object {
        var habits = ArrayList<Habit>()
        lateinit var habitAdapter: HabitAdapter
        var firestoreCollectionReference = FirebaseConnection().firebaseDatabase.collection("Habit")
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        habitAdapter = HabitAdapter(this, habits)
        habitsGrid.adapter = habitAdapter

        fetchHabits()
        addHabit()
        progressHabit()
        openHabitInfo()
        markDayChange()
    }

    private fun fetchHabits() {

        firestoreCollectionReference
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result!!.forEach { habits.add(it.toObject(Habit::class.java)) }
                    habitAdapter.notifyDataSetChanged()
                }
            }

    }

    private fun addHabit() {

        add.setOnClickListener {
            val habitFormIntent = Intent(this, FormActivity::class.java)
            habitFormIntent.putExtra("PARENT_ACTIVITY_NAME", "MAIN")
            startActivityForResult(habitFormIntent, 1)
        }

    }

    private fun progressHabit() {
        habitsGrid.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                val currentHabit = habits[position]
                currentHabit.updateProgress()

                if (currentHabit.progress.status == Status.COMPLETED.toString()) {
                    changeHabitViewBackgroundColor(habits.indexOf(currentHabit))
                }

                habitAdapter.notifyDataSetChanged()

                val currentHabitDoc = firestoreCollectionReference.document(currentHabit.name)
                currentHabitDoc.update("progress", currentHabit.progress)
                currentHabitDoc.update("stats", currentHabit.stats)

            }

    }

    private fun openHabitInfo() {

        habitsGrid.onItemLongClickListener = OnItemLongClickListener { _, _, position, _ ->
            val habitInfoIntent = Intent(this, InfoActivity::class.java)
            habitInfoIntent.putExtra("PARENT_ACTIVITY_NAME", "MAIN")
            habitInfoIntent.putExtra("habit_name", habits[position].name)
            startActivityForResult(habitInfoIntent, 2)
            true
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        /**
         * 100 for addition
         * 200 for deletion
         * 400 for back-buttoning from Info to Main
         */

        when (resultCode) {

            100 -> {
                firestoreCollectionReference
                    .document(data?.getStringExtra("new_habit_name")!!)
                    .get()
                    .addOnSuccessListener {
                        habits.add(it.toObject(Habit::class.java)!!)
                        habitAdapter.notifyDataSetChanged()
                    }
            }

            200 -> {
                firestoreCollectionReference
                    .document(data!!.getStringExtra("del_habit")!!)
                    .delete()
                    .addOnSuccessListener {
                        habits.removeIf { it.name == data.getStringExtra("del_habit") }
                        habitAdapter.notifyDataSetChanged()
                    }
            }

            400 -> {
                firestoreCollectionReference
                    .document(data?.getStringExtra("current_habit")!!)
                    .get()
                    .addOnSuccessListener {
                        val habit = it.toObject(Habit::class.java)!!
                        habits[habits.indexOf(habit)] = habit
                        habitAdapter.notifyDataSetChanged()
                    }
            }

        }

    }

    private fun markDayChange() {

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, DayChangeReceiver::class.java)
        intent.putParcelableArrayListExtra("all_habits_list", habits)

        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 19
        calendar[Calendar.MINUTE] = calendar.get(Calendar.MINUTE)
        calendar[Calendar.SECOND] = 0

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            (600 * 1000).toLong(),
            pendingIntent

        )

        /**
         * for everyday (24 * 60 * 60 * 1000).toLong(),
         */
    }

    private fun changeHabitViewBackgroundColor(position: Int) {
        habitsGrid[position].background =
            ContextCompat.getDrawable(this, R.drawable.habit_view_border_filled)
    }


}


