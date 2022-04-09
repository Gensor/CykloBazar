package com.gensor.cyklobazar.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.gensor.cyklobazar.R
import com.gensor.cyklobazar.database.Database
import com.gensor.cyklobazar.models.User
import com.gensor.cyklobazar.utils.Constants
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.iv_profile_user_image

class ProfileActivity :  BaseActivity() {

    private var selectedImageFileUri : Uri? = null
    private var database : Database? = null
    private lateinit var loggedUser : User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupActionBar()

        val bundle = intent.getBundleExtra("bundle")
        database = bundle?.getParcelable<Database>("database")
        database?.loadUser(this)

        iv_profile_user_image.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                imageChooser()
            }else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),Constants.READ_STORAGE_PERMISSION_CODE)
            }
        }


        button_profile_save.setOnClickListener {
            profileUpdate()
        }
    }

    /*
    Pred použitím metódy výberu obrázka potrebujem povolenie od používateľa.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ((requestCode == Constants.READ_STORAGE_PERMISSION_CODE)
            && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            imageChooser()
        }else{
            Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    /*
    Metóda sa používa na výber obrázka zo zariadenia.
     */
    private fun imageChooser(){
        val getIntent =  Intent(Intent.ACTION_GET_CONTENT)
        getIntent.setType("image/*")

        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")

        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        startActivityForResult(chooserIntent, Constants.PICK_IMAGE_CODE)
    }

    /*
    Po úspešnom výbere obrázka sa obrázok aktualizuje v activity_profile -> iv_profile_user_image.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_CODE && data!!.data != null){
            selectedImageFileUri = data.data

            Glide
                .with(this)
                .load(selectedImageFileUri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .transform(CircleCrop())
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(iv_profile_user_image)
        }
    }

    /*
    Metóda volaná z triedy databázy, ktorá vyplní textové polia údajmi prihláseného používateľa.
     */
    fun populateFieldsFromDatabase(user : User) {
        loggedUser = user

        Glide
            .with(this)
            .load(user.image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .transform(CircleCrop())
            .placeholder(resources.getDrawable(R.drawable.ic_baseline_person_24,theme))
            .into(iv_profile_user_image)

        et_profile_name.setText(user.name)
        et_profile_email.setText(user.email)

    }

    /*
    Nastavenie toolbaru s funkciou späť.
     */
    private fun setupActionBar(){
        setSupportActionBar(toolbar_profile_top)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_double_arrow_left_24)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        toolbar_profile_top?.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    /*
    Pomenuje obrázok zo zariadenia a uloží ho do databázy.
     */
    private fun uploadUserImage(database : Database){
        val imageByteArray = Constants.reduceImageSize(this, selectedImageFileUri!!)
        database.uploadUserImage(imageByteArray)
    }

    /*
    Aktualizuje údaje používateľa, ak sa nejaké zmenili.
     */
    fun profileUpdate(){
        val user = HashMap<String, Any>()
        var anyChange = false
        if(et_profile_name.text.toString() != loggedUser.name){
            user[Constants.USER_NAME] = et_profile_name.text.toString()
            anyChange = true
        }
        if(et_profile_email.text.toString() != loggedUser.email){
            user[Constants.USER_EMAIL] = et_profile_email.text.toString()
            anyChange = true
        }
        if(selectedImageFileUri != null ) {
            anyChange = true
            database?.let { uploadUserImage(it) }
        }

        if (anyChange == true)
            database?.updateUser(this, user)
    }


}