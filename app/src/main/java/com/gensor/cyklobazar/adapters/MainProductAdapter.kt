package com.gensor.cyklobazar.adapters

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
import com.gensor.cyklobazar.R
import com.gensor.cyklobazar.activities.ProductActivity
import com.gensor.cyklobazar.database.Database
import com.gensor.cyklobazar.models.Product
import kotlinx.coroutines.*

class MainProductAdapter(
    private var listOfProducts : ArrayList<Product>,
    private val context : Context,
    private val database : Database
) : RecyclerView.Adapter<MainProductAdapter.ViewHolder>(){

    private val TAG : String = "MAIN_ADAPTER"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainProductAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_main_ad, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainProductAdapter.ViewHolder, position: Int) {
        val product = listOfProducts.get(position)
        GlobalScope.launch(Dispatchers.Main){
            populateFields(product, holder, position)
        }
    }

    private suspend fun populateFields(product: Product, holder: MainProductAdapter.ViewHolder, position: Int){
        holder.title.text = "${product.brand} ${product.model}"
        Glide
            .with(context)
            .load(product.image)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_insert_photo_24)
            .into(holder.productImage)
        holder.price.text = "Price: ${product.price} €"
        holder.postedBy.text = database.getUserName(product.userId)
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("product", product)
            bundle.putParcelable("database", database)
            context.startActivity(Intent(context, ProductActivity::class.java)
                .putExtra("bundle", bundle))
        }
    }

    override fun getItemCount(): Int {
        return listOfProducts.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var title : TextView
        var productImage : ImageView
        var postedBy : TextView
        var price : TextView

        init{
            title = itemView.findViewById(R.id.tv_main_main_brand)
            productImage = itemView.findViewById(R.id.iv_main_main_productImage)
            price = itemView.findViewById(R.id.tv_main_main_price)
            postedBy = itemView.findViewById(R.id.tv_main_main_createdBy)

        }
    }

}