package com.gensor.cyklobazar.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.gensor.cyklobazar.R

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signupButton = findViewById<Button>(R.id.button_main_signup)
        signupButton.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        val loginButton = findViewById<Button>(R.id.button_main_login)
        loginButton.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}