package com.gensor.cyklobazar.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.gensor.cyklobazar.R
import com.gensor.cyklobazar.activities.Ad.AdActivity
import com.gensor.cyklobazar.adapters.MainProductAdapter
import com.gensor.cyklobazar.database.Database
import com.gensor.cyklobazar.database.FirestoreClass
import com.gensor.cyklobazar.models.Product
import com.gensor.cyklobazar.models.User
import com.gensor.cyklobazar.utils.Session
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main_content.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.coroutines.launch

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var database : Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database = FirestoreClass()
        setupActionBar()
        database.loadUser(this)
        nav_view.setNavigationItemSelectedListener(this)

        lifecycleScope.launch {
            rv_main.visibility = View.GONE
            progressBar_main.visibility = View.VISIBLE

            database.getAllAds(this@MainActivity)

            progressBar_main.visibility = View.GONE
            rv_main.visibility = View.VISIBLE
        }
    }

    fun showProducts(array: ArrayList<Product>){
        rv_main.layoutManager = LinearLayoutManager(this)
        rv_main.setHasFixedSize(true)
        val adapter = MainProductAdapter(array, this, database)
        rv_main.adapter = adapter
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            doubleBackToExit()
        }
    }

    /*
    Funkcie v??etk??ch tla??idiel bo??n??ho menu.
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val bundle = Bundle()
        bundle.putParcelable("database",database)

        when(item.itemId){
            R.id.nav_view_main_login -> {
                startActivity(Intent(this, LoginActivity::class.java)
                    .putExtra("bundle",bundle)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                finish()
            }
            R.id.nav_view_main_register -> {
                startActivity(Intent(this, RegisterActivity::class.java)
                    .putExtra("bundle",bundle)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                finish()
            }
            R.id.nav_view_main_logout -> {
                database.signOut(this)
                updateUserInMenu(Session.LOGOUT)
            }
            R.id.nav_view_main_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java)
                    .putExtra("bundle",bundle)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                finish()
            }
            R.id.nav_view_main_add_ad -> {
                startActivity(Intent(this, AdActivity::class.java)
                    .putExtra("bundle",bundle)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                finish()
            }
            R.id.nav_view_main_myAds -> {
                startActivity(Intent(this, MyAdsActivity::class.java)
                    .putExtra("bundle",bundle)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    /*
    Nastavenie toolbaru s funkciou otv??rania bo??n??ho menu.
     */
    private fun setupActionBar(){
        setSupportActionBar(toolbar_main_activity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_reorder_24)
            actionBar.setDisplayShowTitleEnabled(true)
        }
        toolbar_main_activity?.setNavigationOnClickListener {
            database.loadUser(this)
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    /*
    Zobraz?? bo??n?? menu v z??vislosti od toho, ??i je pou????vate?? prihl??sen?? alebo nie.
     */
    fun updateUserInMenu(session: Session, user: User = User()){
        when(session){
            Session.LOGIN -> {
                if (user.id.isNotEmpty()) {
                    Glide
                        .with(this)
                        .load(user.image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .transform(CircleCrop())
                        .into(iv_user_image)

                    tv_username.text = user.name
                    nav_view.menu.setGroupVisible(R.id.activity_main_drawer_logged, true)
                    nav_view.menu.setGroupVisible(R.id.activity_main_drawer_sign_out, false)
                }
            }
            Session.LOGOUT -> {
                nav_view.menu.setGroupVisible(R.id.activity_main_drawer_logged, false)
                nav_view.menu.setGroupVisible(R.id.activity_main_drawer_sign_out, true)
                Glide.with(this).clear(iv_user_image)
                tv_username.text = ""
            }
        }
    }
}