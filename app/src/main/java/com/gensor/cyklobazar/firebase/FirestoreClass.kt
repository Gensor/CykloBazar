package com.gensor.cyklobazar.firebase

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.gensor.cyklobazar.activities.LoginActivity
import com.gensor.cyklobazar.activities.MainActivity
import com.gensor.cyklobazar.activities.RegisterActivity
import com.gensor.cyklobazar.models.User
import com.gensor.cyklobazar.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class FirestoreClass{
    private val fireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User){
        fireStore.collection(Constants.USERS)
            .document(getUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess() }

    }

    fun loginUser(activity: Activity){
        val id = getUserId()
        if(id == "")return
        fireStore.collection(Constants.USERS)
            .document(id)
            .get()
            .addOnSuccessListener {
                document ->
                val loggedUser = document.toObject(User::class.java)!!
                when(activity){
                    //TODO: co keby som pouzil strategy pattern activity.login()
                    is LoginActivity -> {activity.loginSuccess()}
                    is MainActivity -> {activity.updateUserInMenu(loggedUser)}
                }

            }.addOnFailureListener { e ->
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                    is MainActivity -> {
                        activity.hideProgressDialog()
                    }
                }
            }
    }

    fun signOut(){
        FirebaseAuth.getInstance().signOut();
    }

    fun getUserId() : String {
        val user = FirebaseAuth.getInstance().currentUser
        var userId = ""
        if (user != null){
            userId = user.uid
        }
        return userId
    }
}