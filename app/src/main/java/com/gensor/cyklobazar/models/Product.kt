package com.gensor.cyklobazar.models

import android.os.Parcelable

interface Product : Parcelable{
    val image : String
    val brand : String
    val model : String
    val price : Long
    val userId : String
}