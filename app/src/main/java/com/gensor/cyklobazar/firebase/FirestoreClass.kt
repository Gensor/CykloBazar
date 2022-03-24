package com.gensor.cyklobazar.firebase

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

    fun getUserId() : String = FirebaseAuth.getInstance().currentUser!!.uid
}