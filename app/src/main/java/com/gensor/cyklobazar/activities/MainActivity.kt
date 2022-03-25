package com.gensor.cyklobazar.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import androidx.drawerlayout.widget.DrawerLayout
import com.gensor.cyklobazar.R
import com.google.android.material.navigation.NavigationView

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sideMenu = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toolbarButton = findViewById<Button>(R.id.app_bar_main_toolbar_icon)
        //open side menu
        toolbarButton.setOnClickListener {  sideMenu.openDrawer(Gravity.LEFT)}

        //todo: if getinstance == true tak menuPrihlaseny nacitaj a ak false tak menuOdhlaseny

        /*val signupButton = findViewById<Button>(R.id.button_main_signup)
        signupButton.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        val loginButton = findViewById<Button>(R.id.button_main_login)
        loginButton.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }*/
    }
}