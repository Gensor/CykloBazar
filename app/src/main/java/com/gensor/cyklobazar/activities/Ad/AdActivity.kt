package com.gensor.cyklobazar.activities.Ad

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.allViews
import com.gensor.cyklobazar.R
import com.gensor.cyklobazar.activities.BaseActivity
import com.gensor.cyklobazar.activities.MainActivity
import kotlinx.android.synthetic.main.activity_ad.*

class AdActivity : BaseActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var spinner_productCategory : Spinner
    private lateinit var spinner_bikeCategory : Spinner
    private lateinit var spinner_partsCategory : Spinner
    private var productCategorySelected = ""
    private var bikeCategorySelected = ""
    private var partsCategorySelected = ""

    //observer pre formulare
    private val formViewManager = ViewVisibilityManager()
    private lateinit var ebikeForm : ViewVisibility
    private lateinit var roadBikeForm : ViewVisibility
    private lateinit var mountainBikeForm : ViewVisibility
    private lateinit var forkForm : ViewVisibility
    private lateinit var wheelForm : ViewVisibility

    //observer pre spinnery
    private val spinnerViewManager = ViewVisibilityManager()
    private  lateinit var bikeSpinnerView : ViewVisibility
    private  lateinit var partsSpinnerView : ViewVisibility

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad)
        setupActionBar()
        initCategorySpinner()
        initBikeCategorySpinner()
        initPartsCategorySpinner()

        ebikeForm = ViewVisibility(linearLayout_adActivity_bikes_eBike, formViewManager)
        roadBikeForm = ViewVisibility(linearLayout_adActivity_bikes_roadBike, formViewManager)
        mountainBikeForm = ViewVisibility(linearLayout_adActivity_bikes_mountainBike, formViewManager)
        forkForm = ViewVisibility(linearLayout_adActivity_parts_fork, formViewManager)
        wheelForm = ViewVisibility(linearLayout_adActivity_parts_wheel, formViewManager)

        bikeSpinnerView = ViewVisibility(spinner_bikeCategory, spinnerViewManager)
        partsSpinnerView = ViewVisibility(spinner_partsCategory, spinnerViewManager)

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
        productCategorySelected = spinner_productCategory.setSelection(0).toString()

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
        bikeCategorySelected = spinner_bikeCategory.setSelection(0).toString()
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
        partsCategorySelected = spinner_partsCategory.setSelection(0).toString()
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
                        spinnerViewManager.visibleView = partsSpinnerView
                        formViewManager.visibleView = forkForm
                        spinner_partsCategory.setSelection(0)
                    }
                    "Bikes"-> {
                        spinnerViewManager.visibleView = bikeSpinnerView
                        formViewManager.visibleView = ebikeForm
                        spinner_bikeCategory.setSelection(0)
                    }
                }
            }
            spinner_bikeCategory -> {
                bikeCategorySelected = spinner_bikeCategory.selectedItem.toString()
                when(bikeCategorySelected){
                    "Electric bicycle" -> {
                        formViewManager.visibleView = ebikeForm
                    }
                    "Road bicycle" -> {
                        formViewManager.visibleView = roadBikeForm
                    }
                    "Mountain bicycle" -> {
                        formViewManager.visibleView = mountainBikeForm
                    }
                }
            }
            spinner_partsCategory -> {
                partsCategorySelected = spinner_partsCategory.selectedItem.toString()
                when(partsCategorySelected){
                    "Fork" -> {
                        formViewManager.visibleView = forkForm
                    }
                    "Wheels" -> {
                        formViewManager.visibleView = wheelForm
                    }
                }
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}