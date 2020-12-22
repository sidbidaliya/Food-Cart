package com.sid.foodproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.sid.foodproject.R

class WelcomeSplashActivity : AppCompatActivity() {

    private val splashTimeOut : Long = 3000 // 1sec

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_splash)

        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            startActivity(Intent(this@WelcomeSplashActivity,LoginActivity::class.java))

            // close this activity
            finish()
        }, splashTimeOut)

    }
}