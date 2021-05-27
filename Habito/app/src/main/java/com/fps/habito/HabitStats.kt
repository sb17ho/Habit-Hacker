package com.fps.habito

import android.os.Parcel
import android.os.Parcelable

class HabitStats(var streak: Int = 0, var comp: Int = 0) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<HabitStats> {
            override fun createFromParcel(parcel: Parcel) = HabitStats(parcel)
            override fun newArray(size: Int) = arrayOfNulls<HabitStats>(size)
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
