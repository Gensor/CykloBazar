package com.gensor.cyklobazar.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.gensor.cyklobazar.R
import com.gensor.cyklobazar.database.Database
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    private var database : Database? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val bundle = intent.getBundleExtra("bundle")
        database = bundle?.getParcelable<Database>("database")
        setupActionBar()

        button_login.setOnClickListener{
            loginUser()
        }
    }

    /*
    Nastavenie toolbaru s funkciou späť.
     */
    private fun setupActionBar(){
        setSupportActionBar(toolbar_login_top)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(com.google.android.material.R.drawable.abc_ic_ab_back_material)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        toolbar_login_top?.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    /*
    Prihlásenie používateľa.
     */
    private fun loginUser(){
        val email : String = editTextTextEmailAddress_login.text.toString().trim()
        val password : String = editTextTextPassword_login.text.toString()

        if (validateForm(email, password)){
            showProgressDialog(resources.getString(R.string.please_wait))
            database?.loginUser(this, email, password)
        }
    }

    /*
    Overenie, či sú prihlasovacie údaje kompletné.
     */
    private fun validateForm(email: String, password: String): Boolean{
        return when {
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter a email")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter a password")
                false
            }
            else -> return true
        }
    }

    /*
    Metóda sa spustí po úspešnom prihlásení používateľa.
     */
    fun loginSuccess() {
        hideProgressDialog()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


}