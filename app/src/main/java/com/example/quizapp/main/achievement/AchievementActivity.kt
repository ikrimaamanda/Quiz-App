package com.example.quizapp.main.achievement

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.awesomedialog.*
import com.example.quizapp.R
import com.example.quizapp.base.BaseActivity
import com.example.quizapp.common.Common
import com.example.quizapp.databinding.ActivityAchievementBinding
import com.example.quizapp.dbhelper.DBHelper
import com.example.quizapp.model.QuestionRecyclerView

class AchievementActivity : BaseActivity<ActivityAchievementBinding>() {

    private val backToQuestion:BroadcastReceiver = object :BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent!!.action.toString() == Common.KEY_BACK_FROM_ACHIEVEMENT) {
                val questionIndex = intent.getIntExtra(Common.KEY_BACK_FROM_ACHIEVEMENT, -1)
                goBackActivityWithQuestionIndex(questionIndex)
            }
        }

    }

    private fun goBackActivityWithQuestionIndex(questionIndex: Int) {
        val returnIntent = Intent()
        returnIntent.putExtra(Common.KEY_BACK_FROM_ACHIEVEMENT, questionIndex)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    private val questionList = ArrayList<QuestionRecyclerView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_achievement
        super.onCreate(savedInstanceState)

        LocalBroadcastManager.getInstance(this).registerReceiver(backToQuestion, IntentFilter(Common.KEY_BACK_FROM_ACHIEVEMENT))

        setMenu()

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

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(backToQuestion)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_achievement, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_0 -> viewAnswer()
            R.id.item_1 -> doQuizAgain()
        }
        return true
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
                        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
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
                Log.d("TAG", "positive ")
                val returnIntent = Intent()
                returnIntent.putExtra("action", "doQuizAgain")
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
            .onNegative("No") {
                Log.d("TAG", "negative ")
            }
    }

    private fun viewAnswer() {
        val returnIntent = Intent()
        returnIntent.putExtra("action", "viewAnswer")
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