package com.gensor.cyklobazar.firebase

import com.gensor.cyklobazar.activities.LoginActivity
import com.gensor.cyklobazar.activities.SignUpActivity
import com.gensor.cyklobazar.models.User
import com.gensor.cyklobazar.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class FirestoreClass{
    private val fireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User){
        fireStore.collection(Constants.USERS)
            .document(getUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess() }

    }

    fun loginUser(activity: LoginActivity){
        fireStore.collection(Constants.USERS)
            .document(getUserId())
            .get()
            .addOnSuccessListener {
                document ->
                val logedUser = document.toObject(User::class.java)
                if (logedUser != null) activity.loginSuccess()
            }
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