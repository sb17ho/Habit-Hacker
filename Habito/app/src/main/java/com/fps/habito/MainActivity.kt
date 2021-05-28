package com.fps.habito

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemLongClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import java.util.*

import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val habitsGrid: GridView by lazy { findViewById(R.id.habitsGrid) }
    private val add: ImageView by lazy { findViewById(R.id.add) }

    private lateinit var notificationManagerCompat : NotificationManagerCompat

    companion object{
         var habits = ArrayList<Habit>()
         lateinit var habitAdapter: HabitAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManagerCompat = NotificationManagerCompat.from(this)

        habitAdapter = HabitAdapter(this, habits)
        habitsGrid.adapter = habitAdapter

        add.setOnClickListener {
            val habitFormIntent = Intent(this, FormActivity::class.java)
            habitFormIntent.putExtra("PARENT_ACTIVITY_NAME", "MAIN")
            startActivityForResult(habitFormIntent, 1)
        }


        progressHabit()
        openHabitInfo()

        markDayChange()
    }

    private fun progressHabit() {
        habitsGrid.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            val crntHabit = habits[position]
            crntHabit.updateProgress()

            if(crntHabit.status == Status.COMPLETED){
                changeHabitViewBackgroundColor(habits.indexOf(crntHabit))
            }

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

            val targetHabit = habits.find { it.name == data.getStringExtra("old_habit_for_main") }
            targetHabit?.icon = updatedHabit?.icon!!
            targetHabit?.name = updatedHabit.name
            targetHabit?.desc = updatedHabit.desc
            targetHabit?.steps = updatedHabit.steps
            targetHabit?.stats = Stats(updatedHabit.stats.streak, updatedHabit.stats.comp)

            habitAdapter.notifyDataSetChanged()

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

    private fun changeHabitViewBackgroundColor(position : Int){
        habitsGrid[position].background =  ContextCompat.getDrawable(this, R.drawable.habit_view_border_filled)
    }

    private fun sendNotification(view: View){

        val notification = NotificationCompat.Builder(this, ReminderNotification.CHANNEL_ID)
                .setSmallIcon(R.drawable.home)
                .setContentTitle(title)
                .setContentText("Time to do your habit")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .build()

        notificationManagerCompat.notify(1, notification)

    }


}

