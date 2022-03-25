package com.gensor.cyklobazar.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import androidx.drawerlayout.widget.DrawerLayout
import com.gensor.cyklobazar.R
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //open side menu
        app_bar_main_toolbar_icon.setOnClickListener {  drawer_layout.openDrawer(Gravity.LEFT)}

        //todo: if getinstance == true tak menuPrihlaseny nacitaj a ak false tak menuOdhlaseny

        /*val signupButton = findViewById<Button>(R.id.button_main_signup)
        signupButton.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        val loginButton = findViewById<Button>(R.id.button_main_login)
        loginButton.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }*/
    }
}