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
import com.gensor.cyklobazar.R
import com.gensor.cyklobazar.database.Database
import com.gensor.cyklobazar.models.User
import com.gensor.cyklobazar.utils.Constants
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.iv_profile_user_image

class ProfileActivity :  BaseActivity() {

    private var selectedImageFileUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupActionBar()

        iv_profile_user_image.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                imageChooser()
            }else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),Constants.READ_STORAGE_PERMISSION_CODE)
            }
        }

        val bundle = intent.getBundleExtra("bundle")
        val database = bundle?.getParcelable<Database>("database")
        database?.loadUser(this)
    }

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
            Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT)
        }
    }

    private fun imageChooser(){
        val getIntent =  Intent(Intent.ACTION_GET_CONTENT)
        getIntent.setType("image/*");

        val pickIntent = Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");

        val chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent));

        startActivityForResult(chooserIntent, Constants.PICK_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_CODE && data!!.data != null){
            selectedImageFileUri = data.data

            Glide
                .with(this)
                .load(selectedImageFileUri)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(iv_profile_user_image)
        }
    }

    fun populateFieldsFromDatabase(user : User) {
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_person_24)
            .into(iv_profile_user_image)
        et_profile_name.setText(user.name)
        et_profile_email.setText(user.email)

    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_profile_top)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(com.google.android.material.R.drawable.abc_ic_ab_back_material)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        toolbar_profile_top?.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}