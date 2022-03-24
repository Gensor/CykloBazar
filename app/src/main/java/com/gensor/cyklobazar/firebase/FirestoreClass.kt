package com.gensor.cyklobazar.firebase

import com.gensor.cyklobazar.activities.SignUpActivity
import com.gensor.cyklobazar.models.User
import com.google.firebase.firestore.FirebaseFirestore


class FirestoreClass{
    private val fireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User){

    }
}