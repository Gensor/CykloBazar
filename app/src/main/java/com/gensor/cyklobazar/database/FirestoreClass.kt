package com.gensor.cyklobazar.database

import android.app.Activity
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import com.gensor.cyklobazar.activities.*
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

    constructor(parcel: Parcel) : this() {
    }


    /*
    Registrácia užívateľa.
     */
    override fun registerUser(
        activity: RegisterActivity,
        name: String,
        email: String,
        password: String
    ) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result!!.user!!
                    val registeredEmail = firebaseUser.email!!
                    val user = User(firebaseUser.uid, name, registeredEmail)
                    fireStore.collection(Constants.USERS)
                        .document(getUserId())
                        .set(user, SetOptions.merge())
                        .addOnSuccessListener {
                            activity.userRegisteredSuccess()
                        }
                } else {
                    Toast.makeText(activity, task.exception!!.message, Toast.LENGTH_SHORT).show()
                    activity.hideProgressDialog()
                }
            }
    }

    /*
    Prihlásenie užívateľa.
     */
    override fun loginUser(activity: LoginActivity, email: String, password: String) {
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
                    Toast.makeText(
                        activity.baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    activity.hideProgressDialog()
                }
            }

    }

    /*
    Odhlásenie užívateľa.
     */
    override fun signOut(activity: MainActivity) {
        FirebaseAuth.getInstance().signOut()
        if (getUserId() == "") {
            activity.updateUserInMenu(Session.LOGOUT)
            Toast.makeText(activity, "you are signed out", Toast.LENGTH_SHORT).show()
        }
    }

    /*
    ID prihláseného užívateľa.
     */
    override fun getUserId(): String {
        val user = FirebaseAuth.getInstance().currentUser
        var userId = ""
        if (user != null) {
            userId = user.uid
        }
        return userId
    }

    /*
    Vyplní údaje prihláseného užívateľa v aktivite.
     */
    override fun loadUser(activity: Activity) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser != null) {
            fireStore.collection(Constants.USERS)
                .document(firebaseUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    val loggedUser = document.toObject(User::class.java)!!
                    when (activity) {
                        is MainActivity -> activity.updateUserInMenu(Session.LOGIN, loggedUser)
                        is ProfileActivity -> activity.populateFieldsFromDatabase(loggedUser)
                    }
                }
        }
    }

    /*
    Uloží profilový obrázok do cloudového úložiska a odkaz do databázy.
     */
    override fun uploadUserImage(uri: Uri, filename: String) {

        FirebaseStorage.getInstance().reference.child(Constants.PROFILE_PICTURES + filename)
            .putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uriInStorage ->
                    fireStore.collection(Constants.USERS)
                        .document(getUserId())
                        .update(Constants.USER_IMAGE, uriInStorage.toString())
                }
            }
    }

    /*
    Aktualizovať profil používateľa.
     */
    override fun updateUser(activity: ProfileActivity, userHashMap : HashMap<String, Any>){
        fireStore.collection(Constants.USERS)
            .document(getUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                Log.i(activity.javaClass.simpleName, "updated successfully")
                Toast.makeText(activity, "updated successfully", Toast.LENGTH_SHORT).show()
                loadUser(activity)
            }.addOnFailureListener {
                    error -> activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "error updating")
                Toast.makeText(activity, "error updating", Toast.LENGTH_SHORT).show()
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