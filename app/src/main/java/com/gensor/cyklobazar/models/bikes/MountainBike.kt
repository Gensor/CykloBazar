package com.gensor.cyklobazar.models.bikes

import android.os.Parcel
import android.os.Parcelable
import com.gensor.cyklobazar.models.Product

data class MountainBike (
    val brand: String = "",
    val model: String = "",
    val year: Int = 0,
    val price: Long = 0L,
    val fork: String = "",
    val wheelSize: String = "",
    val dropperPost: Boolean = false,
    val userId : String = ""


    ): Parcelable, Product {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(brand)
        parcel.writeString(model)
        parcel.writeInt(year)
        parcel.writeLong(price)
        parcel.writeString(fork)
        parcel.writeString(wheelSize)
        parcel.writeByte(if (dropperPost) 1 else 0)
        parcel.writeString(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MountainBike> {
        override fun createFromParcel(parcel: Parcel): MountainBike {
            return MountainBike(parcel)
        }

        override fun newArray(size: Int): Array<MountainBike?> {
            return arrayOfNulls(size)
        }
    }


}