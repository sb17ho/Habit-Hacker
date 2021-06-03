package com.fps.habito

import android.os.Parcel
import android.os.Parcelable

class Progress(var progress: Int = 0, var steps: Int = 1, var status: String = Status.NOT_STARTED.toString()) : Parcelable {

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<Progress> {
            override fun createFromParcel(parcel: Parcel) = Progress(parcel)
            override fun newArray(size: Int) = arrayOfNulls<Progress>(size)
        }
    }

    private constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(progress)
        dest?.writeInt(steps)
        dest?.writeString(status)
    }

    override fun toString(): String {
        return "Progress(progress=$progress, steps=$steps, status=$status)"
    }

}