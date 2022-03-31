package com.gensor.cyklobazar.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.gensor.cyklobazar.R
import com.gensor.cyklobazar.database.Database
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    private var database : Database? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setupActionBar()
        val bundle = intent.getBundleExtra("bundle")
        database = bundle?.getParcelable<Database>("database")

        button_register.setOnClickListener{
            registerUser()
        }

    }

    /*
    Nastavenie toolbaru s funkciou späť.
     */
    private fun setupActionBar(){
        setSupportActionBar(toolbar_register)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(com.google.android.material.R.drawable.abc_ic_ab_back_material)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        toolbar_register?.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    /*
    Overenie, či sú informácie pre registráciu vyplnené.
     */
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

    /*
    Po overení údajov používateľa ho zaregistruje do databázy.
     */
    private fun registerUser(){
        val name: String = editTextTextPersonName_register.text.toString().trim()
        val email: String = editTextTextEmailAddress_register.text.toString().trim()
        val password: String = editTextTextPassword_register.text.toString()

        if(validateForm(name, email, password)){
            showProgressDialog(resources.getString(R.string.please_wait))
            database?.registerUser(this, name, email, password)
        }
    }
    /*
    Keď sa používateľ úspešne zaregistruje , potvrdí sa registrácia textom na obrazovke a prepne
    ma do MainActivity.
     */
    fun userRegisteredSuccess(){
        Toast.makeText(
            this, "succesfully registered",
            Toast.LENGTH_LONG
        ).show()
        hideProgressDialog()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}