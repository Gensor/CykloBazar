package com.gensor.cyklobazar.models.bikes

import android.os.Parcel
import android.os.Parcelable
import com.gensor.cyklobazar.models.Product

data class RoadBike(
    val brand : String = "",
    val model : String = "",
    val year : Int = 0,
    val price : Long = 0,
    val groupSet : String = "",
    val size : String = "",
    val userId : String = ""
) : Parcelable, Product
{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(brand)
        parcel.writeString(model)
        parcel.writeInt(year)
        parcel.writeLong(price)
        parcel.writeString(groupSet)
        parcel.writeString(size)
        parcel.writeString(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RoadBike> {
        override fun createFromParcel(parcel: Parcel): RoadBike {
            return RoadBike(parcel)
        }

        override fun newArray(size: Int): Array<RoadBike?> {
            return arrayOfNulls(size)
        }
    }

}