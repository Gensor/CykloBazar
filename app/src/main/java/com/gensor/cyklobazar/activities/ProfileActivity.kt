package com.gensor.cyklobazar.activities

import android.content.Intent
import android.os.Bundle
import com.gensor.cyklobazar.R
import com.gensor.cyklobazar.database.Database
import com.gensor.cyklobazar.models.User
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity :  BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupActionBar()

        val bundle = intent.getBundleExtra("bundle")
        val database = bundle?.getParcelable<Database>("database")
        database?.loadUser(this)
    }

    fun populateFieldsFromDatabase(user : User) {
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