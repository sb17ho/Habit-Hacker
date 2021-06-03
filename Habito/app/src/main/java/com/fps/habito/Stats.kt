package com.fps.habito

import android.os.Parcel
import android.os.Parcelable
import java.util.*
import java.util.concurrent.TimeUnit

class Stats(var streak: Int = 0, var comp: Int = 0) : Parcelable {

    var startDate: Date = Calendar.getInstance().time

    val allTime: Double
        get() {

            val timeElapsed = if (TimeUnit.DAYS.convert(Calendar.getInstance().time.time - startDate.time, TimeUnit.MILLISECONDS) == 0L) {
                1
            } else {
                TimeUnit.DAYS.convert(Calendar.getInstance().time.time - startDate.time, TimeUnit.MILLISECONDS)
            }

            return (comp.toDouble()) / timeElapsed

        }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<Stats> {
            override fun createFromParcel(parcel: Parcel) = Stats(parcel)
            override fun newArray(size: Int) = arrayOfNulls<Stats>(size)
        }
    }

    private constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(streak)
        dest?.writeInt(comp)
    }

    override fun toString(): String {
        return "HabitStats(streak=$streak, comp=$comp)"
    }

}
