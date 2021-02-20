package com.example.quizapp.main.questions

import android.content.Intent
import android.os.Bundle
import com.example.quizapp.R
import com.example.quizapp.adapter.TabPagerAdapter
import com.example.quizapp.base.BaseActivity
import com.example.quizapp.databinding.ActivityMainContentQuestionBinding
import com.example.quizapp.main.category.CategoryQuestionActivity

class MainContentQuestionActivity : BaseActivity<ActivityMainContentQuestionBinding>() {

    private lateinit var pagerAdapter: TabPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_main_content_question
        super.onCreate(savedInstanceState)

        pagerAdapter =
            TabPagerAdapter(
                supportFragmentManager
            )
        addFragment()

        clickListener()
    }

    private fun clickListener() {
        binding.btnFinish.setOnClickListener {
            startActivity(Intent(this, CategoryQuestionActivity::class.java))
            finish()
        }
    }

    private fun addFragment() {
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
}