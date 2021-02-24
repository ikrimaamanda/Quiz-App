package com.example.quizapp.main.achievement

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.PopupMenu
import com.example.awesomedialog.*
import com.example.quizapp.R
import com.example.quizapp.base.BaseActivity
import com.example.quizapp.common.Common
import com.example.quizapp.databinding.ActivityAchievementBinding
import com.example.quizapp.dbhelper.DBHelper
import com.example.quizapp.main.category.CategoryQuestionActivity
import com.example.quizapp.model.QuestionRecyclerView

class AchievementActivity : BaseActivity<ActivityAchievementBinding>() {

    private val questionList = ArrayList<QuestionRecyclerView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_achievement
        super.onCreate(savedInstanceState)

        setMenu()

        getData()

        binding.tvTotalQuestion.text = "Question ${Common.rightAnswerCount + Common.wrongAnswerCount}/${Common.questionList.size}"
        binding.tvScore.text = "Score ${Common.rightAnswerCount * (100/Common.questionList.size)}"
        binding.tvTrue.text = "True ${Common.rightAnswerCount}"
        binding.tvFalse.text = "False ${Common.noAnswerCount}"

        if (Common.questionAchievementList.size > 0) {
            initData()
            setRecyclerView()
        }
    }

    override fun onBackPressed() {
        AwesomeDialog.build(this)
            .title("Back to Category")
            .body("Are you sure to delete this data?")
            .position(AwesomeDialog.POSITIONS.CENTER)
            .onPositive("Yes") {
                Common.fragmentList.clear()
                Common.answerSheetList.clear()
                intent<CategoryQuestionActivity>(this)
                super.onBackPressed()
            }
            .onNegative("No") {
            }
    }

    private fun setMenu() {
        binding.ivMenu.setOnClickListener {
            val popup = PopupMenu(this, binding.ivMenu)
            popup.inflate(R.menu.menu_achievement)
            popup.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.item_0 -> {
                        viewAnswer()
                    }
                    R.id.item_1 -> {
                        doQuizAgain()
                    }
                }
                true
            }
            popup.show()
        }
    }

    private fun doQuizAgain() {
        AwesomeDialog.build(this)
            .title("Do Quiz Again")
            .body("Are you sure to delete this data?")
            .position(AwesomeDialog.POSITIONS.CENTER)
            .onPositive("Yes") {
                val returnIntent = Intent()
                returnIntent.putExtra("action", "doquizagain")
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
            .onNegative("No") {
            }
    }

    private fun viewAnswer() {
        val returnIntent = Intent()
        returnIntent.putExtra("action", "viewanswer")
        returnIntent.putExtra("score", binding.tvScore.text.toString())
        val resultCode = Activity.RESULT_OK
        setResult(resultCode, returnIntent)
        finish()
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