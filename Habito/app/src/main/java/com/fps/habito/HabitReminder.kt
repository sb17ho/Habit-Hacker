package com.fps.habito

import android.os.Parcel
import android.os.Parcelable

data class HabitReminder(var hour: Int = -1, var min: Int = -1, var meridiem: String = "") : Parcelable {

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<HabitReminder> {
            override fun createFromParcel(parcel: Parcel) = HabitReminder(parcel)
            override fun newArray(size: Int) = arrayOfNulls<HabitReminder>(size)
        }
    }

    private constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(hour)
        dest?.writeInt(min)
        dest?.writeString(meridiem)
    }

    fun isSet(): Boolean = !(hour == -1 && min == -1 && meridiem.isEmpty())


    override fun toString(): String {
        return "$hour:$min $meridiem"
    }

}