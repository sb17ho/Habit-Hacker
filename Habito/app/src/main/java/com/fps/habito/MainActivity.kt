package com.fps.habito

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.*
import android.widget.AdapterView.OnItemLongClickListener
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var settings: Button
    private lateinit var howTo: Button
    private lateinit var about: Button
    private lateinit var help: Button
    private lateinit var logout: Button
    private lateinit var closeButton: ImageView
    private lateinit var userImage: ImageView
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private val popupDialog: Dialog by lazy { Dialog(this) }
    private lateinit var bundle: Bundle

    private val habitsGrid: GridView by lazy { findViewById(R.id.habitsGrid) }
    private val add: TextView by lazy { findViewById(R.id.add) }
    private val userImageView: ImageView by lazy { findViewById(R.id.user_email_image_view) }

    companion object {
        val habits by lazy { ArrayList<Habit>() }
    }

    val habitAdapter: HabitAdapter by lazy { HabitAdapter(this, habits) }
    val firestore = FirebaseFirestore.getInstance()


    private lateinit var mGoogleAuth: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerReceiver(resultGiver, IntentFilter("NotifyAndBackup"))

        habitsGrid.adapter = habitAdapter

        googleSignIn()

        bundle = intent.extras!!
        Glide.with(this).load(bundle.get("UserPhoto")).into(userImageView)
        userImageView.setOnClickListener {
            popUpHandle()
        }

        getFireStoreData()


    }

    private fun googleSignIn() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleAuth = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(this, gso)


    }

    private fun startFormActivity() {
        val habitFormIntent = Intent(this, FormActivity::class.java)
        habitFormIntent.putExtra("PARENT_ACTIVITY_NAME", "MAIN")
        startActivityForResult(habitFormIntent, 1)
    }


    private fun startInfoActivity(position: Int) {
        val habitInfoIntent = Intent(this, InfoActivity::class.java)
        habitInfoIntent.putExtra("PARENT_ACTIVITY_NAME", "MAIN")
        habitInfoIntent.putExtra("habit_info", habits[position])
        startActivityForResult(habitInfoIntent, 2)
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            100 -> {
                val newHabit = data?.getParcelableExtra<Habit>("new_habit")!!

                if (!habits.contains(newHabit)) {
                    habits.add(newHabit)
                    println("new habit values $newHabit")
                    habitAdapter.notifyDataSetChanged()
                }

                firestore
                    .collection("Habits")
                    .document(newHabit.name)
                    .set(newHabit)
                    .addOnSuccessListener {
                    }
            }

            200 -> {

                val delHabitName = data!!.getStringExtra("del_habit")!!

                habits.removeIf { it.name == delHabitName }
                habitAdapter.notifyDataSetChanged()

                firestore
                    .collection("Habits")
                    .document(delHabitName)
                    .delete()
                    .addOnSuccessListener {
                    }
            }

            300 -> {

                val updatedHabit = data!!.getParcelableExtra<Habit>("habit_for_main")!!

                habits[habits.indexOf(updatedHabit)] = updatedHabit
                habitAdapter.notifyDataSetChanged()

                firestore
                    .collection("Habits")
                    .document(updatedHabit.name)
                    .set(updatedHabit)
                    .addOnSuccessListener {
                    }

            }

        }

    }

    private fun getFireStoreData() {

        firestore
            .collection("Habits")
            .get()
            .addOnSuccessListener {

                it.documents.forEach { documentSnapshot ->
                    val fetchedHabit = documentSnapshot.toObject(Habit::class.java)!!
                    if (!habits.contains(fetchedHabit)) {
                        habits.add(fetchedHabit)
                    }
                }

                habitAdapter.notifyDataSetChanged()

                add.setOnClickListener {
                    startFormActivity()
                }

                habitsGrid.onItemClickListener =
                    AdapterView.OnItemClickListener { _, _, position, _ ->
                        habits[position].updateProgress()
                        habitAdapter.notifyDataSetChanged()
                    }


                habitsGrid.onItemLongClickListener = OnItemLongClickListener { _, _, position, _ ->
                    startInfoActivity(position)
                    true
                }

                onDayChange()


            }

    }

    private fun popUpHandle() {

        popupDialog.setContentView(R.layout.user_info_layout)
        settings = popupDialog.findViewById(R.id.settingsButton)
        howTo = popupDialog.findViewById(R.id.howToUseButton)
        about = popupDialog.findViewById(R.id.aboutButton)
        help = popupDialog.findViewById(R.id.helpButton)
        logout = popupDialog.findViewById(R.id.logoutButton)
        closeButton = popupDialog.findViewById(R.id.closebutton)
        userImage = popupDialog.findViewById(R.id.userImageView)
        userName = popupDialog.findViewById(R.id.userNameView)
        userEmail = popupDialog.findViewById(R.id.userEmailView)

        val displayMetric = DisplayMetrics()
        val windManager = getSystemService(WINDOW_SERVICE) as WindowManager

        //To make this work for APIs older than based on Android R
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                display?.getRealMetrics(displayMetric)
            }
        } catch (e: NoSuchMethodError) {
            windManager.defaultDisplay.getRealMetrics(displayMetric)
        }
        popupDialog.show()

        userName.text = bundle.getString("UserName")
        userEmail.text = bundle.getString("UserEmail")
        Glide.with(this).load(bundle.get("UserPhoto")).into(userImage)

        logout.setOnClickListener {
            mGoogleAuth.signOut().addOnCompleteListener {
                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                mAuth.signOut()
                val intentToSignIn = Intent(this, GoogleSignIn::class.java)
                startActivity(intentToSignIn)
                finish()
            }
        }

        closeButton.setOnClickListener { popupDialog.dismiss() }
    }

    private var resultGiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent) {

            habitAdapter.notifyDataSetChanged()

            habits.forEach {
                firestore
                    .collection("Habits")
                    .document(it.name).set(it)
            }
        }

    }

    private fun onDayChange() {

        val onDayChangeIntent = Intent(this, HabitsReseter::class.java)

        sendBroadcast(onDayChangeIntent)

        val pendingIntent = PendingIntent.getBroadcast(this, 0, onDayChangeIntent, 0)

        (getSystemService(ALARM_SERVICE) as AlarmManager).setRepeating(
            AlarmManager.RTC_WAKEUP,
            getMidnight().timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

    }

    private fun getMidnight(): Calendar {
        val midnight = Calendar.getInstance()
        midnight[Calendar.HOUR_OF_DAY] = 0
        midnight[Calendar.MINUTE] = 0
        midnight[Calendar.SECOND] = 0
        return midnight
    }


}