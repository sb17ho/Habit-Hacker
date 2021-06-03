package com.fps.habito

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.*
import android.widget.AdapterView.OnItemLongClickListener
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val habitsGrid: GridView by lazy { findViewById(R.id.habitsGrid) }
    private val add: TextView by lazy { findViewById(R.id.add) }

    private val habits = ArrayList<Habit>()
    companion object {
        lateinit var habitAdapter: HabitAdapter
    }

    private lateinit var mGoogleAuth: GoogleSignInClient
    private val firestoreConnection = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.vib_red_pink)))
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.statusBarColor = resources.getColor(R.color.vib_red_pink)

        googleSignIn()

        habitAdapter = HabitAdapter(this, habits)
        habitsGrid.adapter = habitAdapter

        getFireStoreData()

        addButtonOnClickListener()
        progressHabit()
        startInfoActivity()
        markDayChange()
    }

    private fun googleSignIn() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleAuth = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(this, gso)

    }

    private fun addButtonOnClickListener() {
        add.setOnClickListener {
            val habitFormIntent = Intent(this, FormActivity::class.java)
            habitFormIntent.putExtra("PARENT_ACTIVITY_NAME", "MAIN")
            startActivityForResult(habitFormIntent, 1)
        }
    }

    private fun progressHabit() {
        habitsGrid.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                val crntHabit = habits[position]
                crntHabit.updateProgress()

                habitAdapter.notifyDataSetChanged()

            }

    }

    private fun startInfoActivity() {

        habitsGrid.onItemLongClickListener = OnItemLongClickListener { a, b, position, d ->
            val habitInfoIntent = Intent(this, InfoActivity::class.java)
            habitInfoIntent.putExtra("PARENT_ACTIVITY_NAME", "MAIN")
            habitInfoIntent.putExtra("habit_info", habits[position])
            startActivityForResult(habitInfoIntent, 2)
            true
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            100 -> {
                val newHabit = data?.getParcelableExtra<Habit>("new_habit")!!

                if (!habits.contains(newHabit)) {
                    habits.add(newHabit)
                    habitAdapter.notifyDataSetChanged()
                }

                firestoreConnection
                    .collection("Habits")
                    .document(newHabit.name)
                    .set(newHabit)
                    .addOnSuccessListener {
                        Log.d("FireStoreHabitAddition", newHabit.name)
                    }
            }

            200 -> {

                val delHabitName = data!!.getStringExtra("del_habit")!!

                habits.removeIf { it.name == delHabitName }
                habitAdapter.notifyDataSetChanged()

                firestoreConnection
                    .collection("Habits")
                    .document(delHabitName)
                    .delete()
                    .addOnSuccessListener {
                        Log.d("FireStoreHabitDeletion", delHabitName)
                    }
            }

            400 -> {

                val updatedHabit = data!!.getParcelableExtra<Habit>("habit_for_main")!!

                habits[habits.indexOf(updatedHabit)] = updatedHabit
                habitAdapter.notifyDataSetChanged()

                firestoreConnection
                    .collection("Habits")
                    .document(updatedHabit.name)
                    .set(updatedHabit)
                    .addOnSuccessListener {
                        Log.d("FireStoreHabitUpdation", updatedHabit.name)
                    }

            }

        }

    }

    private fun getFireStoreData() {

        firestoreConnection.collection("Habits").addSnapshotListener { value, error ->
            value?.documents?.forEach {

                val habit = (it.toObject(Habit::class.java))

                if (habit != null) {
                    if (habits.contains(habit)) {
                        habits[habits.indexOf(habit)] = habit
                    } else {
                        habits.add(habit)
                    }
                }

            }
            habitAdapter.notifyDataSetChanged()
        }

    }

    private fun markDayChange() {

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, DayChangeReceiver::class.java)
        intent.putParcelableArrayListExtra("all_habits", habits)

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

}





