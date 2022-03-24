package com.example.cyklobazar.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cyklobazar.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setupActionBar()
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_signup_top)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(com.google.android.material.R.drawable.abc_ic_ab_back_material)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        toolbar_signup_top?.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}