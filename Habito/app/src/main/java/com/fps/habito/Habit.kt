package com.fps.habito

import android.os.Parcel
import android.os.Parcelable

class Habit(
    val name: String,
    var desc: String = "",
    var icon: Int = R.drawable.nil,
    var progress: Progress = Progress(),
    var stats: Stats = Stats(),
    var reminder: Reminder = Reminder()
) : Parcelable {

    fun updateProgress() {

        if (progress.progress < progress.steps) {
            ++progress.progress
            progress.status = Status.IN_PROGRESS.toString()
        }

        if (progress.progress == progress.steps && progress.status != Status.COMPLETED.toString()) {
            progress.status = Status.COMPLETED.toString()
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
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readParcelable<Progress>(Progress::class.java.classLoader)!!,
        parcel.readParcelable<Stats>(Stats::class.java.classLoader)!!,
        parcel.readParcelable<Reminder>(Reminder::class.java.classLoader)!!,
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeString(desc)
        dest?.writeInt(icon)
        dest?.writeParcelable(progress, flags)
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
        result = 31 * result + progress.steps
        return result
    }

    override fun toString(): String {
        return "Habit(icon=$icon, name='$name', desc='$desc', progress=$progress, stats=$stats, reminder=$reminder)"
    }


}