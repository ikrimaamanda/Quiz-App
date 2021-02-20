package com.example.quizapp.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.example.quizapp.R
import com.example.quizapp.base.BaseActivity
import com.example.quizapp.databinding.ActivitySplashScreenBinding
import com.example.quizapp.welcomepage.WelcomePageActivity

class SplashScreenActivity : BaseActivity<ActivitySplashScreenBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_splash_screen
        super.onCreate(savedInstanceState)

        Handler(mainLooper).postDelayed(
            {
                startActivity(Intent(this, WelcomePageActivity::class.java))
                finish()
            }, 3000)
    }
}