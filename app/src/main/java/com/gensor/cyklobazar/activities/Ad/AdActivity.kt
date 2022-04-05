package com.gensor.cyklobazar.activities.Ad

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.gensor.cyklobazar.R
import com.gensor.cyklobazar.activities.BaseActivity
import com.gensor.cyklobazar.activities.MainActivity
import com.gensor.cyklobazar.database.Database
import com.gensor.cyklobazar.factories.ProductFactory
import kotlinx.android.synthetic.main.activity_ad.*

class AdActivity : BaseActivity(), AdapterView.OnItemSelectedListener, View.OnClickListener {
    private lateinit var spinner_productCategory : Spinner
    private lateinit var spinner_bikeCategory : Spinner
    private lateinit var spinner_partsCategory : Spinner
    private var productCategorySelected = ""
    private var bikeCategorySelected = ""
    private var partsCategorySelected = ""
    private var productData = HashMap<String, Any>()
    private var database : Database? = null

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

        val bundle = intent.getBundleExtra("bundle")
        database = bundle?.getParcelable<Database>("database")

        ebikeForm = ViewVisibility(linearLayout_adActivity_bikes_eBike, formViewManager)
        roadBikeForm = ViewVisibility(linearLayout_adActivity_bikes_roadBike, formViewManager)
        mountainBikeForm = ViewVisibility(linearLayout_adActivity_bikes_mountainBike, formViewManager)
        forkForm = ViewVisibility(linearLayout_adActivity_parts_fork, formViewManager)
        wheelForm = ViewVisibility(linearLayout_adActivity_parts_wheel, formViewManager)

        bikeSpinnerView = ViewVisibility(spinner_bikeCategory, spinnerViewManager)
        partsSpinnerView = ViewVisibility(spinner_partsCategory, spinnerViewManager)

        button_adActivity_save_ebike.setOnClickListener(this)
        button_adActivity_save_roadBike.setOnClickListener(this)
        button_adActivity_save_fork.setOnClickListener(this)
        button_adActivity_save_mountainBike.setOnClickListener(this)
        button_adActivity_save_wheel.setOnClickListener(this)
        button_adActivity_cancel.setOnClickListener(this)
        button_adActivity_cancel_fork.setOnClickListener(this)
        button_adActivity_cancel_mountainBike.setOnClickListener(this)
        button_adActivity_cancel_roadBike.setOnClickListener(this)
        button_adActivity_cancel_wheel.setOnClickListener(this)


    }

    override fun onClick(view : View){
        when(view.id){
            R.id.button_adActivity_save_ebike -> {
                productData = getEBikeData()
                val ebike = ProductFactory.getEbike(productData)
                database?.addProduct(ebike)
            }
            R.id.button_adActivity_save_roadBike -> {
                productData = getRoadBikeData()
                val roadBike = ProductFactory.getRoadBike(productData)
                database?.addProduct(roadBike)
            }
            R.id.button_adActivity_save_fork -> {
                productData = getForkData()
                val fork = ProductFactory.getFork(productData)
                database?.addProduct(fork)
            }
            R.id.button_adActivity_save_mountainBike -> {
                productData = getMountainBikeData()
                val mountainBike = ProductFactory.getMountainBike(productData)
                database?.addProduct(mountainBike)
            }
            R.id.button_adActivity_save_wheel -> {
                productData = getWheelData()
                val wheel = ProductFactory.getWheel(productData)
                database?.addProduct(wheel)
            }
            else -> onBackPressed()
        }
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

    private fun getEBikeData() : HashMap<String, Any>{
        val data : HashMap<String, Any> = HashMap()

        val brand = et_ad_ebike_battery.text.toString()
        val model = et_ad_ebike_model.text.toString()
        val year = et_ad_ebike_year.text.toString().toInt()
        val price = et_ad_ebike_price.text.toString().toLong()
        val motor = et_ad_ebike_motor.text.toString()
        val kw = et_ad_ebike_kw.text.toString().toInt()
        val batteryCapacity = et_ad_ebike_battery.text.toString().toInt()

        data.set("brand", brand)
        data.set("model", model)
        data.set("year", year)
        data.set("price", price)
        data.set("motor", motor)
        data.set("kw", kw)
        data.set("batteryCapacity", batteryCapacity)
        database?.let { data.set("userId", it.getUserId()) }

        return data
    }

    private fun getRoadBikeData(): HashMap<String, Any> {
        val data : HashMap<String, Any> = HashMap()

        val brand : String = et_ad_roadbike_brand.text.toString()
        val model : String = et_ad_roadbike_model.text.toString()
        val year : Int = et_ad_roadbike_year.text.toString().toInt()
        val price : Long = et_ad_roadbike_price.text.toString().toLong()
        val groupSet : String = et_ad_roadbike_groupSet.text.toString()
        val size : String = et_ad_roadbike_size.text.toString()

        data.set("brand", brand)
        data.set("model", model)
        data.set("year", year)
        data.set("price", price)
        data.set("groupSet", groupSet)
        data.set("size", size)
        database?.let { data.set("userId", it.getUserId()) }

        return data
    }

    private fun getMountainBikeData(): HashMap<String, Any> {
        val data : HashMap<String, Any> = HashMap()

        val brand: String = et_ad_mountainBike_brand.text.toString()
        val model: String = et_ad_mountainBike_model.text.toString()
        val year: Int = et_ad_mountainBike_year.text.toString().toInt()
        val price: Long = et_ad_mountainBike_price.text.toString().toLong()
        val fork: String = et_ad_mountainBike_fork.text.toString()
        val wheelSize: String = et_ad_mountainBike_wheelSize.text.toString()
        val dropperPost: Boolean = switch_ad_mountainBike_dropperPost.isChecked()

        data.set("brand", brand)
        data.set("model", model)
        data.set("year", year)
        data.set("price", price)
        data.set("fork", fork)
        data.set("wheelSize", wheelSize)
        data.set("dropperPost", dropperPost)
        database?.let { data.set("userId", it.getUserId()) }

        return data
    }

    private fun getForkData(): HashMap<String, Any> {
        val data : HashMap<String, Any> = HashMap()

        val brand : String = et_ad_parts_fork_brand.text.toString()
        val model : String = et_ad_parts_fork_model.text.toString()
        val travel : Int = et_ad_parts_fork_travel.text.toString().toInt()
        val price : Long = et_ad_parts_fork_price.text.toString().toLong()

        data.set("brand", brand)
        data.set("model", model)
        data.set("price", price)
        data.set("travel", travel)
        database?.let { data.set("userId", it.getUserId()) }

        return data
    }

    private fun getWheelData(): HashMap<String, Any> {
        val data : HashMap<String, Any> = HashMap()

        val brand : String = et_ad_parts_wheel_brand.text.toString()
        val model : String = et_ad_parts_wheel_model.text.toString()
        val size : String = et_ad_parts_wheel_size.text.toString()
        val material : String = et_ad_parts_wheel_material.text.toString()
        val price : Long = et_ad_parts_wheel_price.text.toString().toLong()

        data.set("brand", brand)
        data.set("model", model)
        data.set("price", price)
        data.set("size", size)
        data.set("material", material)
        database?.let { data.set("userId", it.getUserId()) }

        return data
    }

}