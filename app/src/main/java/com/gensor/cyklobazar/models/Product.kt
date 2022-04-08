package com.gensor.cyklobazar.models

interface Product {
    val image : String
    val brand : String
    val model : String
    val price : Long
    val userId : String
}