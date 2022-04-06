package com.gensor.cyklobazar.models.bikes

import android.os.Parcel
import android.os.Parcelable
import com.gensor.cyklobazar.models.Product

data class EBike (
    val brand: String = "",
    val model: String = "",
    val year: Int = 0,
    val price: Long = 0L,
    val motor: String = "",
    val kw: Int = 0,
    val batteryCapacity: Int = 0,
    val userId : String = "",
    val image : String = ""
): Parcelable,Product{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(brand)
        parcel.writeString(model)
        parcel.writeInt(year)
        parcel.writeLong(price)
        parcel.writeString(motor)
        parcel.writeInt(kw)
        parcel.writeInt(batteryCapacity)
        parcel.writeString(userId)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EBike> {
        override fun createFromParcel(parcel: Parcel): EBike {
            return EBike(parcel)
        }

        override fun newArray(size: Int): Array<EBike?> {
            return arrayOfNulls(size)
        }
    }

}