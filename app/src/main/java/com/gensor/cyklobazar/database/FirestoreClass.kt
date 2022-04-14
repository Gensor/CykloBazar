package com.gensor.cyklobazar.database

import android.app.Activity
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import com.gensor.cyklobazar.activities.*
import com.gensor.cyklobazar.activities.Ad.AdActivity
import com.gensor.cyklobazar.models.Product
import com.gensor.cyklobazar.models.User
import com.gensor.cyklobazar.models.bikes.EBike
import com.gensor.cyklobazar.models.bikes.MountainBike
import com.gensor.cyklobazar.models.bikes.RoadBike
import com.gensor.cyklobazar.models.parts.Fork
import com.gensor.cyklobazar.models.parts.Wheel
import com.gensor.cyklobazar.utils.Constants
import com.gensor.cyklobazar.utils.Session
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class FirestoreClass() : Database {
    private val fireStore = FirebaseFirestore.getInstance()
    private var auth: FirebaseAuth = Firebase.auth
    private val TAG = "DATABASE"

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
                    Toast.makeText(activity, task.exception!!.message, Toast.LENGTH_LONG).show()
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
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
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
    override fun uploadUserImage(imageBytes: ByteArray) {
        val fileName = "PROFILE_IMAGE" + System.currentTimeMillis()

        FirebaseStorage.getInstance().reference.child(Constants.PROFILE_PICTURES + fileName)
            .putBytes(imageBytes)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uriInStorage ->
                    fireStore.collection(Constants.USERS)
                        .document(getUserId())
                        .update(Constants.USER_IMAGE, uriInStorage.toString())
                }
            }
    }

    override suspend fun uploadProductImage(imageBytes: ByteArray, activity: AdActivity) {
        val fileName = "PRODUCT_IMAGE" + System.currentTimeMillis()
        val reference = FirebaseStorage.getInstance().reference.child(Constants.PRODUCT_IMAGES + fileName)

        reference.putBytes(imageBytes).await()
        val url = reference.downloadUrl.await().toString()
        activity.setImageUrl(url)

    }

    /*
    Aktualizovať profil používateľa.
     */
    override fun updateUser(activity: ProfileActivity, userHashMap : HashMap<String, Any>){
        fireStore.collection(Constants.USERS)
            .document(getUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                Toast.makeText(activity, "updated successfully", Toast.LENGTH_SHORT).show()
                loadUser(activity)
            }.addOnFailureListener {
                    error -> activity.hideProgressDialog()
                Toast.makeText(activity, "error updating", Toast.LENGTH_SHORT).show()
            }
        if (userHashMap[Constants.USER_EMAIL] != null){
            val user = auth.currentUser
            if (user != null) {
                user.updateEmail(userHashMap[Constants.USER_EMAIL].toString())
            }
        }
    }

    override suspend fun addProduct(product: Product) {
        when(product){
            is EBike -> {
                addProductByType(Constants.EBIKE, product)
            }
            is MountainBike -> {
                addProductByType(Constants.MOUNTAINBIKE, product)
            }
            is RoadBike -> {
                addProductByType(Constants.ROADBIKE, product)
            }
            is Fork -> {
                addProductByType(Constants.FORK, product)
            }
            is Wheel -> {
                addProductByType(Constants.WHEEL, product)
            }
        }
    }

    private suspend fun addProductByType(type: String, product: Product){
        val ref = fireStore.collection(type)
        val documentReference = ref.add(product).await()
        ref.document(documentReference.id).set(hashMapOf("id" to documentReference.id), SetOptions.merge())
    }

    /*
    metoda na vymazanie dokumentu z kolekcie podla id
     */
    override fun deleteProduct(product: Product) {
        if(product.image.isNotEmpty()){
            FirebaseStorage.getInstance()
                .getReferenceFromUrl(product.image)
                .delete()
        }

        when(product){
            is EBike -> {
                fireStore.collection(Constants.EBIKE).document(product.id).delete()
            }
            is MountainBike -> {
                fireStore.collection(Constants.MOUNTAINBIKE).document(product.id).delete()
            }
            is RoadBike -> {
                fireStore.collection(Constants.ROADBIKE).document(product.id).delete()
            }
            is Fork -> {
                fireStore.collection(Constants.FORK).document(product.id).delete()
            }
            is Wheel -> {
                fireStore.collection(Constants.WHEEL).document(product.id).delete()
            }
        }
    }

    /*
    metoda na ziskanie vsetkych uzivatelom pridanych produktov.
     */
    override suspend fun getMyAds(activity: MyAdsActivity) {
        val array = ArrayList<Product>()

        for (colection in collections){
            val documents = fireStore.collection(colection).whereEqualTo(Constants.PRODUCT_USER_ID, getUserId()).get().await()
            documents.documents.forEach {
                when(colection){
                    Constants.EBIKE -> { array.add(it.toObject(EBike::class.java)!!)}
                    Constants.FORK -> { array.add(it.toObject(Fork::class.java)!!)}
                    Constants.MOUNTAINBIKE -> { array.add(it.toObject(MountainBike::class.java)!!)}
                    Constants.ROADBIKE -> { array.add(it.toObject(RoadBike::class.java)!!)}
                    Constants.WHEEL -> { array.add(it.toObject(Wheel::class.java)!!)}
                }
            }
        }
        activity.showProducts(array)

    }

    /*
    metoda na ziskanie vsetkych produktov v bazary
     */
    override suspend fun getAllAds(activity: MainActivity) {
        val array = ArrayList<Product>()

        for (colection in collections){
            val documents = fireStore.collection(colection).get().await()
            documents.documents.forEach {
                when(colection){
                    Constants.EBIKE -> { array.add(it.toObject(EBike::class.java)!!)}
                    Constants.FORK -> { array.add(it.toObject(Fork::class.java)!!)}
                    Constants.MOUNTAINBIKE -> { array.add(it.toObject(MountainBike::class.java)!!)}
                    Constants.ROADBIKE -> { array.add(it.toObject(RoadBike::class.java)!!)}
                    Constants.WHEEL -> { array.add(it.toObject(Wheel::class.java)!!)}
                }
            }
        }

        activity.showProducts(array)
    }

    override suspend fun getUserEmail(productId : String): String {
        var email = ""

        for (colection in collections){
            val document = fireStore.collection(colection).document(productId).get().await()

            if (document.exists()){
                val userId = document.getString(Constants.PRODUCT_USER_ID)
                val user = fireStore.collection(Constants.USERS).document(userId!!).get().await()
                email = user.getString(Constants.USER_EMAIL)!!
            }
        }
        return email
    }

    override suspend fun getUserName(id: String): String{
        val ref = fireStore.collection(Constants.USERS).document(id)
        return ref.get().await().getString(Constants.USER_NAME)!!
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