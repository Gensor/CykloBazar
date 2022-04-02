package com.gensor.cyklobazar.models.parts

import android.os.Parcel
import android.os.Parcelable

data class Fork (
    private val brand : String = "",
    private val model : String = "",
    private val travel : Int = 0,
    private val price : Long = 0L
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readLong()
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Fork> {
        override fun createFromParcel(parcel: Parcel): Fork {
            return Fork(parcel)
        }

        override fun newArray(size: Int): Array<Fork?> {
            return arrayOfNulls(size)
        }
    }

}