package com.gensor.cyklobazar.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.gensor.cyklobazar.R
import com.gensor.cyklobazar.firebase.FirestoreClass

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val typeFace : Typeface = Typeface.createFromAsset(assets,"league-gothic.regular.ttf")
        val tv_app_name = findViewById<TextView>(R.id.tv_app_name)
        tv_app_name.typeface = typeFace

        Handler(Looper.getMainLooper()).postDelayed({
            val currentUserID = FirestoreClass().getUserId()
            if (currentUserID.isNotEmpty()){
                //todo: naloguj ho do sidebaru
            }
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1500)
    }
}