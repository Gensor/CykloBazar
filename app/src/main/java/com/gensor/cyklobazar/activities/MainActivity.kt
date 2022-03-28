package com.gensor.cyklobazar.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.gensor.cyklobazar.R
import com.gensor.cyklobazar.database.Database
import com.gensor.cyklobazar.database.FirestoreClass
import com.gensor.cyklobazar.models.User
import com.gensor.cyklobazar.utils.Session
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    val database : Database = FirestoreClass()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupActionBar()
        nav_view.setNavigationItemSelectedListener(this)
        database.loadUser(this)
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            doubleBackToExit()
        }
    }

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

                //TODO: do samostatnej fun
                nav_view.getMenu().setGroupVisible(R.id.activity_main_drawer_logged, false)
                nav_view.getMenu().setGroupVisible(R.id.activity_main_drawer_sign_out, true)
                iv_user_image.setImageResource(R.drawable.ic_baseline_person_24)
                tv_username.text = ""
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

    fun updateUserInMenu(session: Session, user: User?){
        when(session){
            Session.LOGIN -> {
                if (user != null) {
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
            Session.LOGOUT -> {
                nav_view.getMenu().setGroupVisible(R.id.activity_main_drawer_logged, false)
                nav_view.getMenu().setGroupVisible(R.id.activity_main_drawer_sign_out, true)
                iv_user_image.setImageResource(R.drawable.ic_baseline_person_24)
                tv_username.text = ""
            }
        }
    }
}