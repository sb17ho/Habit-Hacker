package com.fps.habito

import android.os.Parcel
import android.os.Parcelable
import java.util.*
import java.util.concurrent.TimeUnit

class Stats(var streak: Int = 0, var comp: Int = 0, val startDate: Date) : Parcelable {

    constructor():this(startDate=Calendar.getInstance().time)

    val allTime: Double
        get() {

            val timeElapsed = if (TimeUnit.DAYS.convert(
                    Calendar.getInstance().time.time - startDate.time,
                    TimeUnit.MILLISECONDS
                ) == 0L
            ) {
                1
            } else {
                TimeUnit.DAYS.convert(
                    Calendar.getInstance().time.time - startDate.time,
                    TimeUnit.MILLISECONDS
                )
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
        Date(parcel.readLong())
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(streak)
        dest?.writeInt(comp)
        dest?.writeLong(startDate.time)
    }

    override fun toString(): String {
        return "Stats(streak=$streak, comp=$comp, startDate=$startDate)"
    }


}
