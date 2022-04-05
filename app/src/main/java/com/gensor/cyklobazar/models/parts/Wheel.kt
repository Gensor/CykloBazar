package com.gensor.cyklobazar.models.parts

import android.os.Parcel
import android.os.Parcelable
import com.gensor.cyklobazar.models.Product

data class Wheel (
    val brand : String = "",
    val model : String = "",
    val size : String = "",
    val material : String = "",
    val price : Long = 0L,
    val userId : String = ""
) : Parcelable, Product
{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(brand)
        parcel.writeString(model)
        parcel.writeString(size)
        parcel.writeString(material)
        parcel.writeLong(price)
        parcel.writeString(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Wheel> {
        override fun createFromParcel(parcel: Parcel): Wheel {
            return Wheel(parcel)
        }

        override fun newArray(size: Int): Array<Wheel?> {
            return arrayOfNulls(size)
        }
    }

}