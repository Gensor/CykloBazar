package com.gensor.cyklobazar.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.gensor.cyklobazar.R
import kotlinx.android.synthetic.main.activity_ad.*

class AdActivity : BaseActivity(), AdapterView.OnItemSelectedListener {
    private var productCategorySelected = ""
    private var bikeCategorySelected = ""
    private var partsCategorySelected = ""
    private lateinit var spinner_productCategory : Spinner
    private lateinit var spinner_bikeCategory : Spinner
    private lateinit var spinner_partsCategory : Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad)
        setupActionBar()
        initCategorySpinner()
        initBikeCategorySpinner()
        initPartsCategorySpinner()
    }

    /*
    Nastavenie toolbaru s funkciou späť.
     */
    private fun setupActionBar(){
        setSupportActionBar(toolbar_ad_top)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(com.google.android.material.R.drawable.abc_ic_ab_back_material)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        toolbar_ad_top?.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


    /*
    Nastavenie spinnera v kategórii produktov
     */
    private fun initCategorySpinner(){
        spinner_productCategory = spinner_adActivity_productCategorySpinner

        ArrayAdapter.createFromResource(this,
            R.array.ad_productCategory_spinner,
            android.R.layout.simple_spinner_item).also {
                adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_productCategory.adapter = adapter
        }
        spinner_productCategory.onItemSelectedListener = this
    }

    /*
    Nastavenie spinnera v kategórii bicyklov
    */
    private fun initBikeCategorySpinner(){
        spinner_bikeCategory = spinner_adActivity_bikeCategorySpinner
        ArrayAdapter.createFromResource(this,
            R.array.ad_bikeCategory_spinner,
            android.R.layout.simple_spinner_item).also {
                adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_bikeCategory.adapter = adapter
        }
        spinner_bikeCategory.onItemSelectedListener = this
    }


    /*
    Nastavenie spinnera v kategórii dielov
    */
    private fun initPartsCategorySpinner(){
        spinner_partsCategory = spinner_adActivity_partsCategorySpinner
        ArrayAdapter.createFromResource(this,
            R.array.ad_partsCategory_spinner,
            android.R.layout.simple_spinner_item).also {
                adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_partsCategory.adapter = adapter
        }
        spinner_partsCategory.onItemSelectedListener = this
    }

    /*
    Zobrazenie formulára na vytvorenie produktu podľa vybraných spinnerov .
     */
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        productCategorySelected = spinner_productCategory.selectedItem.toString()
        when(p0 as Spinner){
            spinner_productCategory -> {
                when(productCategorySelected){
                    "Parts" -> {
                        spinner_bikeCategory.visibility = View.GONE
                        spinner_partsCategory.visibility = View.VISIBLE
                        setCardVisibility(linearLayout_adActivity_parts_fork)
                        spinner_partsCategory.setSelection(0)
                    }
                    "Bikes"-> {
                        spinner_bikeCategory.visibility = View.VISIBLE
                        spinner_partsCategory.visibility = View.GONE
                        setCardVisibility(linearLayout_adActivity_bikes_eBike)
                        spinner_bikeCategory.setSelection(0)
                    }
                }
            }
            spinner_bikeCategory -> {
                bikeCategorySelected = spinner_bikeCategory.selectedItem.toString()
                when(bikeCategorySelected){
                    "Electric bicycle" -> {
                        setCardVisibility(linearLayout_adActivity_bikes_eBike)
                    }
                    "Road bicycle" -> {
                        setCardVisibility(linearLayout_adActivity_bikes_roadBike)
                    }
                    "Mountain bicycle" -> {
                        setCardVisibility(linearLayout_adActivity_bikes_mountainBike)
                    }
                }
            }
            spinner_partsCategory -> {
                partsCategorySelected = spinner_partsCategory.selectedItem.toString()
                when(partsCategorySelected){
                    "Fork" -> {
                        setCardVisibility(linearLayout_adActivity_parts_fork)
                    }
                //TODO: pridat tovar
                }
            }
        }
    }

    /*
    Funkcia vie zobraziť iba jeden formulár a skryť ostatné.
     */
    private fun setCardVisibility(layout : LinearLayout){
        val layouts  = ArrayList<LinearLayout>()

        layouts.add(linearLayout_adActivity_bikes_eBike)
        layouts.add(linearLayout_adActivity_bikes_roadBike)
        layouts.add(linearLayout_adActivity_bikes_mountainBike)
        layouts.add(linearLayout_adActivity_parts_fork)

        layout.visibility = View.VISIBLE
        layouts.map { if(it != layout) it.visibility = View.GONE }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}