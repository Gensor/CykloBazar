package com.gensor.cyklobazar.adapters

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gensor.cyklobazar.R.*
import com.gensor.cyklobazar.database.Database
import com.gensor.cyklobazar.models.Product

open class MyAdsProductAdapter(
    private var listOfProducts : ArrayList<Product>,
    private val context : Context,
    private val database : Database
) : RecyclerView.Adapter<MyAdsProductAdapter.ViewHolder>(){

    private val TAG : String = "PRODUCY_ADAPTER"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdsProductAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layout.activity_my_ads_ad, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyAdsProductAdapter.ViewHolder, position: Int) {
        val product = listOfProducts.get(position)
        Log.i(TAG," product : ${product.toString()}")
        populateFields(product, holder, position)
        holder.itemView.setOnClickListener {
            //TODO: sprav fragment s obrazkom a toStringom
        }
    }

    private fun populateFields(product: Product, holder: MyAdsProductAdapter.ViewHolder, position: Int){
        Glide
            .with(context)
            .load(product.image)
            .centerCrop()
            .placeholder(drawable.ic_baseline_insert_photo_24)
            .into(holder.productImage)

        holder.title.text = "${product.brand} ${product.model}"
        holder.price.text = "Price: ${product.price} €"
        holder.deleteImage.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes"){ dialog, id ->
                    database.deleteProduct(product)
                    listOfProducts.removeAt(position)
                    notifyItemRemoved(position)
                }
                .setNegativeButton("No"){dialog, id ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }
    override fun getItemCount(): Int {
        return listOfProducts.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var title : TextView
        var productImage : ImageView
        var price : TextView
        var deleteImage : ImageView


        init{
            title = itemView.findViewById(id.tv_myAds_ad_name)
            productImage = itemView.findViewById(id.iv_myAds_ad_productImage)
            price = itemView.findViewById(id.tv_myAds_ad_price)
            deleteImage = itemView.findViewById(id.button_myAds_ad_delete)

        }
    }


}