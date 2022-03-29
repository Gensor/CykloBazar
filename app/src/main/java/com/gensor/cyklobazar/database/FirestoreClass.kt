package com.gensor.cyklobazar.database

import android.app.Activity
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import com.gensor.cyklobazar.activities.LoginActivity
import com.gensor.cyklobazar.activities.MainActivity
import com.gensor.cyklobazar.activities.ProfileActivity
import com.gensor.cyklobazar.activities.RegisterActivity
import com.gensor.cyklobazar.models.User
import com.gensor.cyklobazar.utils.Constants
import com.gensor.cyklobazar.utils.Session
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage



class FirestoreClass() : Database {
    private val fireStore = FirebaseFirestore.getInstance()
    private var auth: FirebaseAuth = Firebase.auth
    private var profilePictureURL = ""

    constructor(parcel: Parcel) : this() {
    }

    override fun registerUser(activity: RegisterActivity, name : String, email : String, password : String){

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result!!.user!!
                    val registeredEmail = firebaseUser.email!!
                    val user = User(firebaseUser.uid, name, registeredEmail)
                    fireStore.collection(Constants.USERS)
                        .document(getUserId())
                        .set(user, SetOptions.merge())
                        .addOnSuccessListener {
                            activity.userRegisteredSuccess() }
                } else {
                    Toast.makeText(activity, task.exception!!.message, Toast.LENGTH_SHORT).show()
                    activity.hideProgressDialog()
                }
            }
    }

    override fun loginUser(activity: LoginActivity, email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val id = getUserId()
                    if (id != "") {
                        fireStore.collection(Constants.USERS)
                            .document(id)
                            .get()
                            .addOnSuccessListener { document ->
                                activity.loginSuccess()
                            }.addOnFailureListener { e ->
                                activity.hideProgressDialog()
                            }
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Login", "signInWithEmail:failure", task.exception)
                    Toast.makeText(activity.baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    activity.hideProgressDialog()
                }
            }

    }

    override fun signOut(activity: MainActivity){
        FirebaseAuth.getInstance().signOut()
        if (getUserId() == ""){
            activity.updateUserInMenu(Session.LOGOUT)
            Toast.makeText(activity,"you are signed out",Toast.LENGTH_SHORT).show()
        }
    }

    override fun getUserId() : String {
        val user = FirebaseAuth.getInstance().currentUser
        var userId = ""
        if (user != null){
            userId = user.uid
        }
        return userId
    }

    override fun getUser(id : String) : User{
        var user = User()
        fireStore.collection(Constants.USERS).document(id)
            .get()
            .addOnSuccessListener { document -> user = document.toObject(User::class.java)!! }
        return user
    }

    override fun loadUser(activity: Activity) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser != null){
            fireStore.collection(Constants.USERS)
                .document(firebaseUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    val loggedUser = document.toObject(User::class.java)!!
                    when(activity){
                        is MainActivity -> activity.updateUserInMenu(Session.LOGIN, loggedUser)
                        is ProfileActivity -> activity.populateFieldsFromDatabase(loggedUser)
                    }
                }
        }
    }

    override fun uploadUserImage(uri : Uri, filename : String){

        FirebaseStorage.getInstance().reference.child(filename).putFile(uri)
            .addOnSuccessListener {
                    taskSnapshot ->
                taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                        uriInStorage ->

                    //TODO: toto updatni v user profile
                    //toto
                    uriInStorage.toString()
                }
            }

    }



    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FirestoreClass> {
        override fun createFromParcel(parcel: Parcel): FirestoreClass {
            return FirestoreClass(parcel)
        }

        override fun newArray(size: Int): Array<FirestoreClass?> {
            return arrayOfNulls(size)
        }
    }
}