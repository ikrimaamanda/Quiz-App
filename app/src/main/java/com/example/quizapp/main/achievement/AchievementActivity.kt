package com.example.quizapp.main.achievement

import android.os.Bundle
import com.example.quizapp.R
import com.example.quizapp.base.BaseActivity
import com.example.quizapp.common.Common
import com.example.quizapp.databinding.ActivityAchievementBinding
import com.example.quizapp.dbhelper.DBHelper
import com.example.quizapp.model.QuestionRecyclerView

class AchievementActivity : BaseActivity<ActivityAchievementBinding>() {

    private val questionList = ArrayList<QuestionRecyclerView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_achievement
        super.onCreate(savedInstanceState)

        getData()

        binding.tvTotalQuestion.text = "Question ${Common.rightAnswerCount + Common.wrongAnswerCount}/${Common.questionList.size}"
        binding.tvScore.text = "Score ${Common.rightAnswerCount * (100/Common.questionList.size)}"
        binding.tvTrue.text = "True ${Common.rightAnswerCount}"
        binding.tvFalse.text = "Wrong ${Common.wrongAnswerCount + Common.noAnswerCount}"

        if (Common.questionAchievementList.size > 0) {
            initData()
            setRecyclerView()
        }
    }

    private fun getData() {
        Common.questionAchievementList = DBHelper.getInstance(this).getQuestionAchievementByCategory(this, Common.selectedCategory!!.id!!)
    }

    private fun setRecyclerView() {
        val achievementAdapter = AchievementAdapter(questionList)
        binding.rvQuestion.adapter = achievementAdapter
        binding.rvQuestion.setHasFixedSize(true)
    }

    private fun initData() {
        questionList.addAll(Common.questionAchievementList)
    }


}