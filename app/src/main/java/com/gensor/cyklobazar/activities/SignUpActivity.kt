package com.gensor.cyklobazar.activities

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.gensor.cyklobazar.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setupActionBar()

        button_signUp.setOnClickListener{
            registerUser()
        }

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

    private fun validateForm(name: String, email: String, password: String): Boolean{
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please enter a name")
                false
            }
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter a email")
                false
            }TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter a password")
                false
            }
            else -> return true

        }
    }

    private fun registerUser(){
        val name: String = editTextTextPersonName_signUp.text.toString().trim()
        val email: String = editTextTextEmailAddress_signUp.text.toString().trim()
        val password: String = editTextTextPassword_signUp.text.toString()

        if(validateForm(name, email, password)){
            Toast.makeText(this,
            "registered",
            Toast.LENGTH_SHORT).show()
        }
    }
}