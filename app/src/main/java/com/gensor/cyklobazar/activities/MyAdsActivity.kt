package com.gensor.cyklobazar.activities

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gensor.cyklobazar.R
import com.gensor.cyklobazar.adapters.MyAdsProductAdapter
import com.gensor.cyklobazar.database.Database
import com.gensor.cyklobazar.models.Product
import kotlinx.android.synthetic.main.activity_my_ads.*
import kotlinx.coroutines.launch

class MyAdsActivity : BaseActivity() {
    lateinit var database : Database
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_ads)
        setupActionBar()

        val bundle = intent.getBundleExtra("bundle")
        database = bundle?.getParcelable<Database>("database")!!
        lifecycleScope.launch {
        database.getMyAds(this@MyAdsActivity)

        }

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

     fun showProducts(listOfProducts : ArrayList<Product>){

            rv_myAds.layoutManager = LinearLayoutManager(this)
            rv_myAds.setHasFixedSize(true)

            val adapter = MyAdsProductAdapter(listOfProducts, this, database)
            rv_myAds.adapter = adapter

    }
}