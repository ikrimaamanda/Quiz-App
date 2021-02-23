package com.example.quizapp.main.questions

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
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
import com.example.quizapp.main.category.CategoryQuestionActivity
import com.example.quizapp.model.CurrentQuestion
import java.util.concurrent.TimeUnit

class MainContentQuestionActivity : BaseActivity<ActivityMainContentQuestionBinding>() {

    private var countDownTimer : CountDownTimer? = null
    var timePlayQuiz = Common.TOTAL_TIME
    lateinit var answerAdapter : AnswerAdapter
    var isAnswerModeView = false

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
                        if (isScrollingDirectionRight) {
                            questionFragment = Common.fragmentList[p0-1]
                            position = p0-1
                        } else if (isScrollingDirectionLeft) {
                            questionFragment = Common.fragmentList[p0+1]
                            position = p0+1
                        } else {
                            questionFragment = Common.fragmentList[p0]
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

                        binding.tvTotalQuestion.text = ("Question\n${Common.rightAnswerCount + Common.wrongAnswerCount}/${Common.questionList.size}")
                        binding.tvScore.text = "Score ${Common.rightAnswerCount * (100/Common.questionList.size)}"
                        binding.tvTrue.text = "True ${Common.rightAnswerCount}"
                        binding.tvFalse.text = "Wrong ${Common.wrongAnswerCount}"

                        if (questionState.type != Common.ANSWER_TYPE.NO_ANSWER) {
                            questionFragment.showCorrectAnswer()
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

            binding.tvTotalQuestion.text = ("Question\n${Common.rightAnswerCount + Common.wrongAnswerCount}/${Common.questionList.size}")
            binding.tvTrue.text = "True ${Common.rightAnswerCount}"
        }

        clickListener()
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

        Log.d("answer:", Common.answerSheetList.toString())
        for (item : CurrentQuestion in Common.answerSheetList) {
            Log.d("itemType:", item.type.toString())
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

        }
    }

    fun finishQuiz() {
        val position = binding.viewPager.currentItem
        val questionFragment = Common.fragmentList[position]

        val questionState = questionFragment.selectedAnswer()
        Common.answerSheetList[position] = questionState
        answerAdapter.notifyDataSetChanged()

        countCorrectAnswer()

        binding.tvTotalQuestion.text = ("Question\n${Common.rightAnswerCount + Common.wrongAnswerCount}/${Common.questionList.size}")
        val score = Common.rightAnswerCount * (100/Common.questionList.size)
        binding.tvScore.text = "Score ${(100 - score) + score}"
        binding.tvTrue.text = "True ${Common.rightAnswerCount}"
        binding.tvFalse.text = "Wrong ${Common.wrongAnswerCount}"

        if (questionState.type != Common.ANSWER_TYPE.NO_ANSWER) {
            questionFragment.showCorrectAnswer()
            questionFragment.disableAnswer()
        }
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
                        Log.d("TAG", "positive ")
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
                        .onPositive("Go to Category") {
                            Log.d("TAG", "positive ")
                            finishQuiz()
//                            intent<CategoryQuestionActivity>(this)
//                            finish()
                        }
                        .onNegative("No") {
                            Log.d("TAG", "negative ")
                        }
            } else {
                finishQuiz()
            }

        }
    }

}