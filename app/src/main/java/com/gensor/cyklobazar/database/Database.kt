package com.gensor.cyklobazar.database

import android.app.Activity
import android.net.Uri
import android.os.Parcelable
import com.gensor.cyklobazar.activities.LoginActivity
import com.gensor.cyklobazar.activities.MainActivity
import com.gensor.cyklobazar.activities.ProfileActivity
import com.gensor.cyklobazar.activities.RegisterActivity
import com.gensor.cyklobazar.models.User

interface Database : Parcelable{
    fun registerUser(activity: RegisterActivity, name : String, email : String, password : String)
    fun loginUser(activity: LoginActivity, email: String, password: String)
    fun signOut(activity: MainActivity)
    fun getUserId() : String
    fun loadUser(activity: Activity)
    fun uploadUserImage(uri : Uri, filename : String)
    fun updateUser(activity: ProfileActivity, userHashMap : HashMap<String, Any>)
}