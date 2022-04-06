package com.gensor.cyklobazar.models.parts

import android.os.Parcel
import android.os.Parcelable
import com.gensor.cyklobazar.models.Product

data class Fork (
    val brand : String = "",
    val model : String = "",
    val travel : Int = 0,
    val price : Long = 0L,
    val userId : String = "",
    val image : String = ""

): Parcelable, Product{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(brand)
        parcel.writeString(model)
        parcel.writeInt(travel)
        parcel.writeLong(price)
        parcel.writeString(userId)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
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