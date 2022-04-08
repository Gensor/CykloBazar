package com.gensor.cyklobazar.database

import android.app.Activity
import android.os.Parcelable
import com.gensor.cyklobazar.activities.*
import com.gensor.cyklobazar.activities.Ad.AdActivity
import com.gensor.cyklobazar.models.Product

interface Database : Parcelable{
    fun registerUser(activity: RegisterActivity, name : String, email : String, password : String)
    fun loginUser(activity: LoginActivity, email: String, password: String)
    fun signOut(activity: MainActivity)
    fun getUserId() : String
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