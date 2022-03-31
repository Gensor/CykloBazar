package com.gensor.cyklobazar.activities

import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.net.Uri
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
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.io.File

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

    /*
    Funkcie všetkých tlačidiel bočného menu.
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
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    /*
    Nastavenie toolbaru s funkciou otvárania bočného menu.
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
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    /*
    Zobrazí bočné menu v závislosti od toho, či je používateľ prihlásený alebo nie.
     */
    fun updateUserInMenu(session: Session, user: User = User()){
        when(session){
            Session.LOGIN -> {
                if (user.id.isNotEmpty()) {
                    val storage = FirebaseStorage.getInstance()
                    val httpsReference = storage.getReferenceFromUrl(
                        "https://firebasestorage.googleapis.com/v0/b/cyklobazar-285ff.appspot.com/o/PROFILE_IMAGE1648660076154.png?alt=media&token=efbe651f-a76a-46e0-8e01-7f2a453c5713")
                    val file = File.createTempFile("tempProfile", ".png")
                    httpsReference.getFile(file)

                    Glide
                        .with(this)
                        .load(file)
                        .centerCrop()
                        .placeholder(resources.getDrawable( R.drawable.ic_baseline_person_24, theme))
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