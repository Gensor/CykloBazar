package com.gensor.cyklobazar.models.parts

import android.os.Parcel
import android.os.Parcelable
import com.gensor.cyklobazar.models.Product

data class Wheel (
    override val id : String = "",
    override val brand : String = "",
    override val model : String = "",
    val size : String = "",
    val material : String = "",
    override val price : Long = 0L,
    override val userId : String = "",
    override val image : String = ""
) : Parcelable, Product {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(brand)
        parcel.writeString(model)
        parcel.writeString(size)
        parcel.writeString(material)
        parcel.writeLong(price)
        parcel.writeString(userId)
        parcel.writeString(image)
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

    override fun toString(): String {
        return """
            <b>SIZE</b>: $size<br>
            <b>MATERIAL</b>: $material
        """.trimIndent()
    }

}