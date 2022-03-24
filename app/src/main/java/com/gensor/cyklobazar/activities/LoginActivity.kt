package com.gensor.cyklobazar.activities

import android.os.Bundle
import com.gensor.cyklobazar.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupActionBar()
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_login_top)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(com.google.android.material.R.drawable.abc_ic_ab_back_material)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        toolbar_login_top?.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}