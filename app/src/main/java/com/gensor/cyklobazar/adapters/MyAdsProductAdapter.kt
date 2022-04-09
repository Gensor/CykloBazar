package com.gensor.cyklobazar.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gensor.cyklobazar.R.*
import com.gensor.cyklobazar.activities.ProductActivity
import com.gensor.cyklobazar.database.Database
import com.gensor.cyklobazar.models.Product

open class MyAdsProductAdapter(
    private var listOfProducts : ArrayList<Product>,
    private val context : Context,
    private val database : Database
) : RecyclerView.Adapter<MyAdsProductAdapter.ViewHolder>(){

    private val TAG : String = "PRODUCT_ADAPTER"
    private val list = listOfProducts

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdsProductAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layout.activity_my_ads_ad, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyAdsProductAdapter.ViewHolder, position: Int) {
        val product = list.get(position)
        populateFields(product, holder, position)
        holder.deleteImage.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes"){ dialog, id ->
                    database.deleteProduct(product)
                    list.removeAt(position)
                    notifyDataSetChanged()
                }
                .setNegativeButton("No"){dialog, id ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun populateFields(product: Product, holder: MyAdsProductAdapter.ViewHolder, position: Int){
        Glide
            .with(context)
            .load(product.image)
            .centerCrop()
            .placeholder(drawable.ic_baseline_insert_photo_24)
            .into(holder.productImage)

        holder.brand.text = "${product.brand}"
        holder.model.text = "${product.model}"
        holder.price.text = "Price: ${product.price} €"

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("product", product)
            bundle.putParcelable("database", database)
            context.startActivity(Intent(context, ProductActivity::class.java)
                .putExtra("bundle", bundle))
        }

    }
    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var brand : TextView
        var productImage : ImageView
        var model : TextView
        var price : TextView
        var deleteImage : ImageView


        init{
            brand = itemView.findViewById(id.tv_myAds_ad_brand)
            model = itemView.findViewById(id.tv_myAds_ad_model)
            productImage = itemView.findViewById(id.iv_myAds_ad_productImage)
            price = itemView.findViewById(id.tv_myAds_ad_price)
            deleteImage = itemView.findViewById(id.button_myAds_ad_delete)

        }
    }


}