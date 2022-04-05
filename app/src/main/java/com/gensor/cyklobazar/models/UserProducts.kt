package com.gensor.cyklobazar.models

import android.os.Parcel
import android.os.Parcelable

data class UserProducts(
    val userId : String = "",
    val products : ArrayList<String> = ArrayList()
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.createStringArrayList()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserProducts> {
        override fun createFromParcel(parcel: Parcel): UserProducts {
            return UserProducts(parcel)
        }

        override fun newArray(size: Int): Array<UserProducts?> {
            return arrayOfNulls(size)
        }
    }
}
