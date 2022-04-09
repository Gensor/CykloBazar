package com.gensor.cyklobazar.activities.Ad

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.gensor.cyklobazar.R
import com.gensor.cyklobazar.activities.BaseActivity
import com.gensor.cyklobazar.activities.MainActivity
import com.gensor.cyklobazar.database.Database
import com.gensor.cyklobazar.factories.ProductFactory
import com.gensor.cyklobazar.utils.Constants
import kotlinx.android.synthetic.main.activity_ad.*
import kotlinx.android.synthetic.main.product_ebike.*
import kotlinx.android.synthetic.main.product_fork.*
import kotlinx.android.synthetic.main.product_mountain_bike.*
import kotlinx.android.synthetic.main.product_road_bike.*
import kotlinx.android.synthetic.main.product_wheel.*
import kotlinx.coroutines.launch

class AdActivity : BaseActivity(), AdapterView.OnItemSelectedListener, View.OnClickListener {
    private lateinit var spinner_productCategory : Spinner
    private lateinit var spinner_bikeCategory : Spinner
    private lateinit var spinner_partsCategory : Spinner
    private var productCategorySelected = ""
    private var bikeCategorySelected = ""
    private var partsCategorySelected = ""
    private var productData = HashMap<String, Any>()
    private var database : Database? = null
    private var imageUrl = ""
    private var selectedImageFileUri : Uri? = null

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

    private val TAG = "AD_ACTIVITY"

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
        button_adActivity_image.setOnClickListener(this)

    }

    override fun onClick(view : View){
        lifecycleScope.launch {

            when(view.id){
                R.id.button_adActivity_save_ebike -> {
                    showProgressDialog("Please wait")
                    productData = getEBikeData()
                    uploadProductImage()
                    val ebike = ProductFactory.createProduct(Constants.EBIKE, productData)
                    if (ebike != null)
                        database?.addProduct(ebike)
                    else Toast.makeText(this@AdActivity, "Failed to save product",Toast.LENGTH_LONG).show()
                    hideProgressDialog()
                    onBackPressed()

                }
                R.id.button_adActivity_save_roadBike -> {
                    showProgressDialog("Please wait")
                    productData = getRoadBikeData()
                    uploadProductImage()
                    val roadBike = ProductFactory.createProduct(Constants.ROADBIKE, productData)
                    if (roadBike != null)
                        database?.addProduct(roadBike)
                    else Toast.makeText(this@AdActivity, "Failed to save product",Toast.LENGTH_LONG).show()
                    hideProgressDialog()
                    onBackPressed()
                }
                R.id.button_adActivity_save_fork -> {
                    showProgressDialog("Please wait")
                    productData = getForkData()
                    uploadProductImage()
                    val fork = ProductFactory.createProduct(Constants.FORK, productData)
                    if(fork != null)
                        database?.addProduct(fork)
                    else Toast.makeText(this@AdActivity, "Failed to save product",Toast.LENGTH_LONG).show()
                    hideProgressDialog()
                    onBackPressed()
                }
                R.id.button_adActivity_save_mountainBike -> {
                    showProgressDialog("Please wait")
                    productData = getMountainBikeData()
                    uploadProductImage()
                    val mountainBike = ProductFactory.createProduct(Constants.MOUNTAINBIKE, productData)
                    if(mountainBike != null)
                        database?.addProduct(mountainBike)
                    else Toast.makeText(this@AdActivity, "Failed to save product",Toast.LENGTH_LONG).show()
                    hideProgressDialog()
                    onBackPressed()
                }
                R.id.button_adActivity_save_wheel -> {
                    showProgressDialog("Please wait")
                    productData = getWheelData()
                    uploadProductImage()
                    val wheel = ProductFactory.createProduct(Constants.WHEEL, productData)
                    if(wheel != null)
                        database?.addProduct(wheel)
                    else Toast.makeText(this@AdActivity, "Failed to save product",Toast.LENGTH_LONG).show()
                    hideProgressDialog()
                    onBackPressed()
                }
                R.id.button_adActivity_image -> {
                    if(ContextCompat.checkSelfPermission(this@AdActivity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        imageChooser()
                    }else {
                        ActivityCompat.requestPermissions(this@AdActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE)
                    }
                }
                else -> onBackPressed()
            }
        }
    }

    fun setImageUrl(url : String){
        imageUrl = url
        productData["image"] = imageUrl
    }

    private suspend fun uploadProductImage(){
        if(selectedImageFileUri != null){
            val imageByteArray = Constants.reduceImageSize(this@AdActivity, selectedImageFileUri!!)
            database?.uploadProductImage(imageByteArray, this@AdActivity)
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
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_double_arrow_left_24)
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

    }

    private fun getEBikeData() : HashMap<String, Any>{
        val data : HashMap<String, Any> = HashMap()
        var year = 0
        var price = 0L
        var kw = 0
        var batteryCapacity = 0

        val brand = et_ad_ebike_brand.text.toString()
        val model = et_ad_ebike_model.text.toString()
        val motor = et_ad_ebike_motor.text.toString()
        if (et_ad_ebike_year.text.toString().isNotEmpty())
            year = et_ad_ebike_year.text.toString().toInt()
        if(et_ad_ebike_price.text.toString().isNotEmpty())
            price = et_ad_ebike_price.text.toString().toLong()
        if(et_ad_ebike_power.text.toString().isNotEmpty())
            kw = et_ad_ebike_power.text.toString().toInt()
        if(et_ad_ebike_battery.text.toString().isNotEmpty())
            batteryCapacity = et_ad_ebike_battery.text.toString().toInt()
        val image = imageUrl

        data.set("brand", brand)
        data.set("model", model)
        data.set("year", year)
        data.set("price", price)
        data.set("motor", motor)
        data.set("kw", kw)
        data.set("batteryCapacity", batteryCapacity)
        database?.let { data.set("userId", it.getUserId()) }
        data.set("image", image)

        return data
    }

    private fun getRoadBikeData(): HashMap<String, Any> {
        val data : HashMap<String, Any> = HashMap()
        var year = 0
        var price = 0L

        val brand : String = et_ad_roadbike_brand.text.toString()
        val model : String = et_ad_roadbike_model.text.toString()
        if(et_ad_roadbike_year.text.toString().isNotEmpty())
            year = et_ad_roadbike_year.text.toString().toInt()
        if(et_ad_roadbike_price.text.toString().isNotEmpty())
            price = et_ad_roadbike_price.text.toString().toLong()
        val groupSet : String = et_ad_roadbike_groupSet.text.toString()
        val size : String = et_ad_roadbike_size.text.toString()
        val image = imageUrl

        data.set("brand", brand)
        data.set("model", model)
        data.set("year", year)
        data.set("price", price)
        data.set("groupSet", groupSet)
        data.set("size", size)
        database?.let { data.set("userId", it.getUserId()) }
        data.set("image", image)

        return data
    }

    private fun getMountainBikeData(): HashMap<String, Any> {
        val data : HashMap<String, Any> = HashMap()
        var year = 0
        var price = 0L
        val brand: String = et_ad_mountainBike_brand.text.toString()
        val model: String = et_ad_mountainBike_model.text.toString()
        if(et_ad_mountainBike_year.text.toString().isNotEmpty())
            year = et_ad_mountainBike_year.text.toString().toInt()
        if(et_ad_mountainBike_price.text.toString().isNotEmpty())
            price = et_ad_mountainBike_price.text.toString().toLong()
        val fork: String = et_ad_mountainBike_fork.text.toString()
        val wheelSize: String = et_ad_mountainBike_wheelSize.text.toString()
        val dropperPost: Boolean = switch_ad_mountainBike_dropperPost.isChecked()
        val image = imageUrl

        data.set("brand", brand)
        data.set("model", model)
        data.set("year", year)
        data.set("price", price)
        data.set("fork", fork)
        data.set("wheelSize", wheelSize)
        data.set("dropperPost", dropperPost)
        database?.let { data.set("userId", it.getUserId()) }
        data.set("image", image)

        return data
    }

    private fun getForkData(): HashMap<String, Any> {
        val data : HashMap<String, Any> = HashMap()
        var price = 0L
        var travel = 0

        val brand : String = et_ad_parts_fork_brand.text.toString()
        val model : String = et_ad_parts_fork_model.text.toString()
        if(et_ad_parts_fork_travel.text.toString().isNotEmpty())
            travel = et_ad_parts_fork_travel.text.toString().toInt()
        if(et_ad_parts_fork_price.text.toString().isNotEmpty())
            price = et_ad_parts_fork_price.text.toString().toLong()
        val image = imageUrl

        data.set("brand", brand)
        data.set("model", model)
        data.set("price", price)
        data.set("travel", travel)
        database?.let { data.set("userId", it.getUserId()) }
        data.set("image", image)

        return data
    }

    private fun getWheelData(): HashMap<String, Any> {
        val data : HashMap<String, Any> = HashMap()
        var price = 0L
        val brand : String = et_ad_parts_wheel_brand.text.toString()
        val model : String = et_ad_parts_wheel_model.text.toString()
        val size : String = et_ad_parts_wheel_size.text.toString()
        val material : String = et_ad_parts_wheel_material.text.toString()
        if(et_ad_parts_wheel_price.text.toString().isNotEmpty())
            price = et_ad_parts_wheel_price.text.toString().toLong()
        val image = imageUrl

        data.set("brand", brand)
        data.set("model", model)
        data.set("price", price)
        data.set("size", size)
        data.set("material", material)
        database?.let { data.set("userId", it.getUserId()) }
        data.set("image", image)

        return data
    }


    private fun imageChooser(){
        val getIntent =  Intent(Intent.ACTION_GET_CONTENT)
        getIntent.setType("image/*")

        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")

        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        startActivityForResult(chooserIntent, Constants.PICK_IMAGE_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_CODE && data!!.data != null){
            selectedImageFileUri = data.data
            button_adActivity_image.text = selectedImageFileUri.toString()
        }
    }

}