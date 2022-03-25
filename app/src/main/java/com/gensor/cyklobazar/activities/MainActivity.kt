package com.gensor.cyklobazar.activities

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.gensor.cyklobazar.R
import com.gensor.cyklobazar.firebase.FirestoreClass
import com.gensor.cyklobazar.models.User
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import com.squareup.picasso.Picasso;

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupActionBar()

        nav_view.setNavigationItemSelectedListener(this)
        FirestoreClass().loginUser(this@MainActivity)
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            doubleBackToExit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_view_main_login -> {
                startActivity(Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                finish()
            }
            R.id.nav_view_main_register -> {
                startActivity(Intent(this, RegisterActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK))
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_main_activity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_reorder_24)
            actionBar.setDisplayShowTitleEnabled(true)
        }
        toolbar_main_activity?.setNavigationOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    fun updateUserInMenu(user: User) {

        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_person_24)
            .into(iv_user_image)
        tv_username.text = user.name

        nav_view.getMenu().setGroupVisible(R.id.activity_main_drawer_logged, true)
        nav_view.getMenu().setGroupVisible(R.id.activity_main_drawer_sign_out, false)

    }
}