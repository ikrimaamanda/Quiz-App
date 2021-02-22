package com.example.quizapp.main.questions

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import com.example.awesomedialog.*
import com.example.quizapp.R
import com.example.quizapp.adapter.MainContentQuestionAdapter
import com.example.quizapp.adapter.TabPagerAdapter
import com.example.quizapp.base.BaseActivity
import com.example.quizapp.common.Common
import com.example.quizapp.databinding.ActivityMainContentQuestionBinding
import com.example.quizapp.dbhelper.DBHelper
import com.example.quizapp.main.category.CategoryQuestionActivity
import java.util.concurrent.TimeUnit

class MainContentQuestionActivity : BaseActivity<ActivityMainContentQuestionBinding>() {

    private lateinit var pagerAdapter: TabPagerAdapter
    private var countDownTimer : CountDownTimer? = null
    var timePlayQuiz = Common.TOTAL_TIME

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_main_content_question
        super.onCreate(savedInstanceState)

        checkQuestions()

        if (Common.questionList.size > 0) {
            binding.tvTimer.visibility = View.VISIBLE
            countTimer()

            generateFragmentList()
            val fragmentAdapter = MainContentQuestionAdapter(supportFragmentManager, this, Common.fragmentList)
            binding.viewPager.offscreenPageLimit = Common.questionList.size
            binding.viewPager.adapter = fragmentAdapter
            binding.tabLayout.setupWithViewPager(binding.viewPager)
        }

        clickListener()
    }

    private fun generateFragmentList() {
        for (i in Common.questionList.indices) {
            val bundle = Bundle()
            bundle.putInt("index", i)
            val fragment = QuestionFragment()
            fragment.arguments = bundle
            Common.fragmentList.add(fragment)
        }
    }

    private fun countTimer() {
        countDownTimer = object : CountDownTimer(Common.TOTAL_TIME.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvTimer.text = (java.lang.String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))

                ))
                timePlayQuiz -= 1000
            }

            override fun onFinish() {
                finishQuiz()
            }

        }
    }

    fun finishQuiz() {

    }

    private fun checkQuestions() {
        Common.questionList = DBHelper.getInstance(this).getQuestionByCategory(this, Common.selectedCategory!!.id!!)

        if (Common.questionList.size == 0) {
            AwesomeDialog.build(this)
                    .title("Ooppss...")
                    .body("We don't have any question for ${Common.selectedCategory!!.name} category")
                    .position(AwesomeDialog.POSITIONS.CENTER)
                    .icon(R.drawable.ic_congrts)
                    .onPositive("Go To Category of Questions") {
                        Log.d("TAG", "positive ")
                        intent<CategoryQuestionActivity>(this)
                        finish()
                    }
        }
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