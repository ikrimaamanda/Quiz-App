package com.example.quizapp.welcomepage

import android.content.Intent
import android.os.Bundle
import com.example.quizapp.R
import com.example.quizapp.base.BaseActivity
import com.example.quizapp.databinding.ActivityWelcomePageBinding
import com.example.quizapp.main.category.CategoryQuestionActivity
import com.example.quizapp.main.questions.MainContentQuestionActivity

class WelcomePageActivity : BaseActivity<ActivityWelcomePageBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_welcome_page
        super.onCreate(savedInstanceState)

        onClick()
    }

    private fun onClick() {
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, MainContentQuestionActivity::class.java))
        }
    }
}