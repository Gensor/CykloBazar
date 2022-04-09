package com.gensor.cyklobazar.activities

import android.os.Bundle
import android.text.Html
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.gensor.cyklobazar.R
import com.gensor.cyklobazar.database.Database
import com.gensor.cyklobazar.models.Product
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.coroutines.launch

class ProductActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        setupActionBar()
        val bundle = intent.getBundleExtra("bundle")
        val product = bundle?.getParcelable<Product>("product")
        val database = bundle?.getParcelable<Database>("database")
        lifecycleScope.launch{
            var email = ""
            if(product != null && database != null){
                email = database.getUserEmail(product.id)
            }

            tv_product_brand.text = product?.brand
            tv_product_model.text = product?.model
            tv_product_price.text = "€ ${product?.price}"
            tv_product_postedBy.text = Html.fromHtml("Posted by <b>$email<b>")
            tv_product_toString.text = Html.fromHtml(product.toString())
        }
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
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_double_arrow_left_1_24)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        toolbar_product_top?.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}