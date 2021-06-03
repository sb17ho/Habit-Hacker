package com.fps.habito

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import android.widget.AdapterView.OnItemLongClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import java.util.*

import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), LifecycleObserver {

    private val habitsGrid: GridView by lazy { findViewById(R.id.habitsGrid) }
    private val add: TextView by lazy { findViewById(R.id.add) }

    //    private val signOut: Button by lazy { findViewById(R.id.sign_out_button) }
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleAuth: GoogleSignInClient

    companion object {
        var habits = ArrayList<Habit>()
        lateinit var habitAdapter: HabitAdapter
    }

    private val firebaseAccess = FirebaseConnection()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        setContentView(R.layout.activity_main)

        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.vib_red_pink)))
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.statusBarColor = resources.getColor(R.color.vib_red_pink)

        onAppBackgrounded()
        onAppForegrounded()

        habitAdapter = HabitAdapter(this, habits)
        habitsGrid.adapter = habitAdapter
        add.setOnClickListener {
            val habitFormIntent = Intent(this, FormActivity::class.java)
            habitFormIntent.putExtra("PARENT_ACTIVITY_NAME", "MAIN")
            startActivityForResult(habitFormIntent, 1)
        }


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleAuth = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(this, gso)

//        signOut.setOnClickListener {
//            mGoogleAuth.signOut().addOnCompleteListener {
//                mAuth = FirebaseAuth.getInstance()
//                mAuth.signOut()
//                val intentToSignIn = Intent(this, GoogleSignIn::class.java)
//                startActivity(intentToSignIn)
//                finish()
//            }
//        }

        progressHabit()
        openHabitInfo()
        markDayChange()
    }

    private fun progressHabit() {
        habitsGrid.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                val crntHabit = habits[position]
                crntHabit.updateProgress()

                habitAdapter.notifyDataSetChanged()

            }

    }


    private fun openHabitInfo() {

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

        /**
         * 100 for addition
         * 200 for deletion
         * 400 for back-buttoning from Info to Main
         */

        if (resultCode == 100) {
            val newHabit = data?.getParcelableExtra<Habit>("new_habit")!!

            if (!habits.contains(newHabit)) {
                habits.add(newHabit)
                habitAdapter.notifyDataSetChanged()
            }

        } else if (resultCode == 200) {
            habits.removeIf { it.name == data!!.getStringExtra("del_habit") }
            habitAdapter.notifyDataSetChanged()
        } else if (resultCode == 400) {

            /**
             * TODO add the feature where the habit is considered changed no matter what field is changed.
             */
            val updatedHabit = data!!.getParcelableExtra<Habit>("habit_for_main")

            val targetHabit =
                habits.find { it.name == data.getStringExtra("old_habit_for_main") }
            targetHabit?.icon = updatedHabit?.icon!!
            targetHabit?.desc = updatedHabit.desc
            targetHabit?.progress!!.steps = updatedHabit.progress.steps
            targetHabit.stats = Stats(updatedHabit.stats.streak, updatedHabit.stats.comp)

            habitAdapter.notifyDataSetChanged()

        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {

        firebaseAccess.firebaseDatabase.collection("Habit").document("nice").get().addOnSuccessListener {

        }
//        val batch = firebaseAccess.firebaseDatabase.batch()
//
//        habits.forEach {
//            batch.set(firebaseAccess.firebaseDatabase.collection("Habit").document(it.name), it)
//        }
//
//        batch.commit()

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        println("in front")
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


}


