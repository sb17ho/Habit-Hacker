package com.fps.habito

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Intent
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
        var habits = ArrayList<Habit>()
        lateinit var habitAdapter: HabitAdapter
        val firestoreConnection = FirebaseFirestore.getInstance()
    }

    private lateinit var mGoogleAuth: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.statusBarColor = resources.getColor(R.color.primary_pink)

        googleSignIn()

        habitAdapter = HabitAdapter(this, habits)
        habitsGrid.adapter = habitAdapter

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
                habits[position].updateProgress()
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

                firestoreConnection
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

                firestoreConnection
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

                firestoreConnection
                    .collection("Habits")
                    .document(updatedHabit.name)
                    .set(updatedHabit)
                    .addOnSuccessListener {
                    }

            }

        }

    }

    private fun getFireStoreData() {

        firestoreConnection
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

                addButtonOnClickListener()
                progressHabit()
                startInfoActivity()
                markDayChange()


            }

    }

    private fun markDayChange() {

        val intent = Intent(this, DayChangeReceiver::class.java)
        intent.putParcelableArrayListExtra("all_habs", habits)
        sendBroadcast(intent)

        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = calendar.get(Calendar.HOUR)
        calendar[Calendar.MINUTE] = calendar.get(Calendar.MINUTE)
        calendar[Calendar.SECOND] = calendar.get(Calendar.SECOND)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_FIFTEEN_MINUTES,
            pendingIntent
        )
    }
}





