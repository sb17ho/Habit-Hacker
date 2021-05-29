package com.fps.habito

import android.os.Parcel
import android.os.Parcelable

class Habit(
        var icon: Int = R.drawable.nil,
        var name: String,
        var desc: String = "",
        var steps: Int = 1,
        var stats: Stats = Stats(),
        var reminder: Reminder = Reminder()
) : Parcelable {

    var progress = 0
    var status = Status.NOT_STARTED

    fun updateProgress() {

        if (progress < steps) {
            ++progress
            status = Status.IN_PROGRESS
        }

        if (progress == steps && status != Status.COMPLETED) {
            status = Status.COMPLETED
            ++stats.streak
            ++stats.comp
        }

    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<Habit> {
            override fun createFromParcel(parcel: Parcel) = Habit(parcel)
            override fun newArray(size: Int) = arrayOfNulls<Habit>(size)
        }
    }

    private constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readParcelable<Stats>(Stats::class.java.classLoader)!!,
            parcel.readParcelable<Reminder>(Reminder::class.java.classLoader)!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(icon)
        dest?.writeString(name)
        dest?.writeString(desc)
        dest?.writeInt(steps)
        dest?.writeParcelable(stats, flags)
        dest?.writeParcelable(reminder, flags)
    }

    override fun equals(other: Any?): Boolean {

        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Habit

        if (name != other.name) return false

        return true

    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + desc.hashCode()
        result = 31 * result + steps
        return result
    }


    override fun toString(): String {
        return "Habit(icon=$icon, name='$name', desc='$desc', steps=$steps, habitStats=$stats, progress=$progress, status='$status' HabitReminder($reminder))"
    }


}