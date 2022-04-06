package com.gensor.cyklobazar.activities

import android.content.Intent
import android.os.Bundle
import com.gensor.cyklobazar.R
import kotlinx.android.synthetic.main.activity_my_ads.*

class MyAdsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_ads)
        setupActionBar()
    }

    /*
    Nastavenie toolbaru s funkciou späť.
     */
    private fun setupActionBar(){
        setSupportActionBar(toolbar_myAds_top)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(com.google.android.material.R.drawable.abc_ic_ab_back_material)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        toolbar_myAds_top?.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}