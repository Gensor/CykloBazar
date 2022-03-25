package com.gensor.cyklobazar.activities

import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.gensor.cyklobazar.R
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupActionBar()

        //todo: if getinstance == true tak menuPrihlaseny nacitaj a ak false tak menuOdhlaseny
        nav_view.setNavigationItemSelectedListener(this)

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
            actionBar.setHomeAsUpIndicator(com.google.android.material.R.drawable.abc_menu_hardkey_panel_mtrl_mult)
            actionBar.setDisplayShowTitleEnabled(true)
        }
        toolbar_main_activity?.setNavigationOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }
}