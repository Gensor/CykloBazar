package com.gensor.cyklobazar.activities

import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.gensor.cyklobazar.R
import com.gensor.cyklobazar.models.Product
import kotlinx.android.synthetic.main.activity_product.*

class ProductActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        setupActionBar()

        val bundle = intent.getBundleExtra("bundle")
        val product = bundle?.getParcelable<Product>("product")
        tv_product_brand.text = product?.brand
        tv_product_model.text = product?.model
        tv_product_price.text = "€ ${product?.price}"
        tv_product_toString.text = product.toString()

        if (product != null) {
            Glide
                .with(this)
                .load(product.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_insert_photo_24)
                .into(iv_product_image)
        }

        button_product_back.setOnClickListener{
            onBackPressed()
        }
    }

    /*
    Nastavenie toolbaru s funkciou späť.
     */
    private fun setupActionBar(){
        setSupportActionBar(toolbar_product_top)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_double_arrow_left_24)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        toolbar_product_top?.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}