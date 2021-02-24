package com.example.quizapp.main.questions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.awesomedialog.*
import com.example.quizapp.R
import com.example.quizapp.adapter.AnswerAdapter
import com.example.quizapp.adapter.MainContentQuestionAdapter
import com.example.quizapp.base.BaseActivity
import com.example.quizapp.common.Common
import com.example.quizapp.databinding.ActivityMainContentQuestionBinding
import com.example.quizapp.dbhelper.DBHelper
import com.example.quizapp.main.achievement.AchievementActivity
import com.example.quizapp.main.category.CategoryQuestionActivity
import com.example.quizapp.model.CurrentQuestion
import com.google.gson.Gson
import java.lang.StringBuilder
import java.util.concurrent.TimeUnit

class MainContentQuestionActivity : BaseActivity<ActivityMainContentQuestionBinding>() {

    private var countDownTimer : CountDownTimer? = null
    var timePlayQuiz = Common.TOTAL_TIME
    lateinit var answerAdapter : AnswerAdapter
    private var isAnswerModeView = false

    private val CODE_GET_RESULT = 7777

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_main_content_question
        super.onCreate(savedInstanceState)

        binding.tvTitle.text = "Category of ${Common.selectedCategory!!.name}"

        checkQuestions()

        if (Common.questionList.size > 0) {
            binding.tvTimer.visibility = View.VISIBLE
            countTimer()

            generateItems()
            setRecyclerViewGridAnswer()

            generateFragmentList()
            setTabAndViewPager()

            binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

                val SCROLLING_RIGHT = 0
                val SCROLLING_LEFT = 1
                val SCROLLING_UNDETERMINED = 2

                var currentScrollDirection = SCROLLING_UNDETERMINED

                private val isScrollingDirectionUndetermined :Boolean
                get() = currentScrollDirection == SCROLLING_UNDETERMINED

                private val isScrollingDirectionRight :Boolean
                get() = currentScrollDirection == SCROLLING_RIGHT

                private val isScrollingDirectionLeft :Boolean
                get() = currentScrollDirection == SCROLLING_LEFT

                private fun setScrollingDirection(positionOffSet : Float) {
                    if(1 - positionOffSet >= 0.5) {
                        this.currentScrollDirection = SCROLLING_RIGHT
                    } else if(1 - positionOffSet <= 0.5) {
                        this.currentScrollDirection = SCROLLING_LEFT
                    }
                }

                override fun onPageScrolled(position: Int, p1: Float, positionOffsetPixels: Int) {
                    if (isScrollingDirectionUndetermined) {
                        setScrollingDirection(p1)
                    }
                }

                override fun onPageSelected(p0: Int) {
                    val questionFragment : QuestionFragment
                    var position = 0
                    if (p0 > 0) {
                        when {
                            isScrollingDirectionRight -> {
                                questionFragment = Common.fragmentList[p0-1]
                                position = p0-1
                            }
                            isScrollingDirectionLeft -> {
                                questionFragment = Common.fragmentList[p0+1]
                                position = p0+1
                            }
                            else -> {
                                questionFragment = Common.fragmentList[p0]
                            }
                        }
                    } else {
                        questionFragment = Common.fragmentList[0]
                        position = 0
                    }

                    if (Common.answerSheetList[position].type == Common.ANSWER_TYPE.NO_ANSWER) {
                        val questionState = questionFragment.selectedAnswer()
                        Common.answerSheetList[position] = questionState
                        answerAdapter.notifyDataSetChanged()

                        countCorrectAnswer()

                        binding.tvTotalQuestion.text = ("Question ${Common.rightAnswerCount + Common.wrongAnswerCount}/${Common.questionList.size}")
                        binding.tvScore.text = "Score ${Common.rightAnswerCount * (100/Common.questionList.size)}"
                        binding.tvTrue.text = "True ${Common.rightAnswerCount}"
                        binding.tvFalse.text = "False ${Common.wrongAnswerCount}"

                        if (questionState.type != Common.ANSWER_TYPE.NO_ANSWER) {
                            questionFragment.disableAnswer()
                        }
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        this.currentScrollDirection = SCROLLING_UNDETERMINED
                    }
                }

            })

            binding.tvTotalQuestion.text = ("Question ${Common.rightAnswerCount + Common.wrongAnswerCount}/${Common.questionList.size}")
            binding.tvTrue.text = "True ${Common.rightAnswerCount}"
        }

        clickListener()
    }

    override fun onBackPressed() {
        AwesomeDialog.build(this)
            .title("Back to Category")
            .body("Are you sure to cancel answer this quiz?")
            .position(AwesomeDialog.POSITIONS.CENTER)
            .onPositive("Yes") {
                this.finish()
                super.onBackPressed()
            }
            .onNegative("No") {
            }
    }

    private fun setTabAndViewPager() {
        val fragmentAdapter = MainContentQuestionAdapter(supportFragmentManager, Common.fragmentList)
        binding.viewPager.offscreenPageLimit = Common.questionList.size
        binding.viewPager.adapter = fragmentAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    private fun setRecyclerViewGridAnswer() {
        binding.rvGridAnswer.setHasFixedSize(true)
        answerAdapter = AnswerAdapter(this, Common.answerSheetList)
        binding.rvGridAnswer.adapter = answerAdapter
        binding.rvGridAnswer.layoutManager = GridLayoutManager(this, if (Common.questionList.size > 5) Common.questionList.size/2 else Common.questionList.size)
    }

    private fun generateItems() {
        for (i in Common.questionList.indices) {
            Common.answerSheetList.add(CurrentQuestion(i, Common.ANSWER_TYPE.NO_ANSWER))
        }
    }

    override fun onDestroy() {
        if (countDownTimer != null) {
            countDownTimer?.cancel()
        }
        Common.fragmentList.clear()
        Common.answerSheetList.clear()
        super.onDestroy()
    }

    private fun countCorrectAnswer() {
        Common.rightAnswerCount = 0
        Common.wrongAnswerCount = 0

        for (item : CurrentQuestion in Common.answerSheetList) {
            if (item.type == Common.ANSWER_TYPE.RIGHT_ANSWER) {
                Common.rightAnswerCount++
            } else if (item.type == Common.ANSWER_TYPE.WRONG_ANSWER) {
                Common.wrongAnswerCount++
            }
        }
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
            @SuppressLint("DefaultLocale")
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

        }.start()
    }

    fun finishQuiz() {
        val position = binding.viewPager.currentItem
        val questionFragment = Common.fragmentList[position]

        val questionState = questionFragment.selectedAnswer()
        Common.answerSheetList[position] = questionState
        answerAdapter.notifyDataSetChanged()

        countCorrectAnswer()

        if (questionState.type != Common.ANSWER_TYPE.NO_ANSWER) {
            questionFragment.showCorrectAnswer()
            questionFragment.disableAnswer()
        }

        Common.timer = Common.TOTAL_TIME - timePlayQuiz
        Common.noAnswerCount = Common.questionList.size - Common.rightAnswerCount
        Common.dataQuestion = StringBuilder(Gson().toJson(Common.answerSheetList))

        val intent = Intent(this, AchievementActivity::class.java)
        startActivityForResult(intent, CODE_GET_RESULT)
    }

    private fun checkQuestions() {
        Common.questionList = DBHelper.getInstance(this).getQuestionByCategory(this, Common.selectedCategory!!.id!!)

        if (Common.questionList.size == 0) {
            AwesomeDialog.build(this)
                    .title("Ooppss...")
                    .body("We don't have any question for ${Common.selectedCategory!!.name} category")
                    .position(AwesomeDialog.POSITIONS.CENTER)
                    .icon(R.drawable.ic_sad)
                    .onPositive("Go To Category of Questions") {
                        intent<CategoryQuestionActivity>(this)
                        finish()
                    }
        }
    }

    private fun clickListener() {
        binding.btnFinish.setOnClickListener {
            if (!isAnswerModeView) {
                AwesomeDialog.build(this)
                    .title("FINISH QUIZ")
                    .body("Are you sure to finish this quiz?")
                    .position(AwesomeDialog.POSITIONS.CENTER)
                    .onPositive("Yes") {
                        finishQuiz()
                    }
                    .onNegative("No") {
                    }
            } else {
                finishQuiz()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_GET_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                val action = data!!.getStringExtra("action")

                if (action == null || TextUtils.isEmpty(action)) {
                    val questionIndex = data.getIntExtra(Common.KEY_BACK_FROM_ACHIEVEMENT, -1)
                    binding.viewPager.currentItem = questionIndex
                    isAnswerModeView = true
                    countDownTimer!!.cancel()

                    binding.tvFalse.visibility = View.GONE
                    binding.tvTrue.visibility = View.GONE
                    binding.tvTimer.visibility = View.GONE

                    for (i in Common.fragmentList.indices) {
                        Common.fragmentList[i].showCorrectAnswer()
                        Common.fragmentList[i].disableAnswer()
                    }
                } else {
                    if (action == "viewanswer") {
                        binding.viewPager.currentItem = 0
                        isAnswerModeView = true
                        countDownTimer!!.cancel()

                        binding.tvScore.text = data.getStringExtra("score")

                        binding.tvFalse.visibility = View.GONE
                        binding.tvTrue.visibility = View.GONE
                        binding.tvTimer.visibility = View.GONE

                        for (i in Common.fragmentList.indices) {
                            Common.fragmentList[i].showCorrectAnswer()
                            Common.fragmentList[i].disableAnswer()
                        }
                    } else if (action == "doquizagain") {
                        binding.viewPager.currentItem = 0
                        isAnswerModeView = true

                        binding.tvFalse.visibility = View.VISIBLE
                        binding.tvTrue.visibility = View.VISIBLE
                        binding.tvTimer.visibility = View.VISIBLE

                        for (i in Common.fragmentList.indices) {
                            Common.fragmentList[i].resetQuestion()
                        }

                        countTimer()

                    }
                }
            }
        }
    }

}