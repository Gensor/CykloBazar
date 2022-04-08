package com.gensor.cyklobazar.database

import android.app.Activity
import android.os.Parcelable
import com.gensor.cyklobazar.activities.*
import com.gensor.cyklobazar.activities.Ad.AdActivity
import com.gensor.cyklobazar.models.Product
import com.gensor.cyklobazar.utils.Constants

interface Database : Parcelable{
    val collections: Array<String>
        get() = arrayOf(Constants.EBIKE, Constants.FORK, Constants.MOUNTAINBIKE, Constants.ROADBIKE, Constants.WHEEL)

    fun registerUser(activity: RegisterActivity, name : String, email : String, password : String)
    fun loginUser(activity: LoginActivity, email: String, password: String)
    fun signOut(activity: MainActivity)
    fun getUserId() : String
    suspend fun getUserEmail(productId : String): String
    fun loadUser(activity: Activity)
    fun uploadUserImage(imageBytes: ByteArray)
    suspend fun uploadProductImage(imageBytes: ByteArray, activity: AdActivity)
    fun updateUser(activity: ProfileActivity, userHashMap : HashMap<String, Any>)
    suspend fun addProduct(product : Product)
    fun deleteProduct(product : Product)
    suspend fun getMyAds(activity: MyAdsActivity)
    suspend fun getAllAds(activity: MainActivity)
    suspend fun getUserName(id: String): String

}