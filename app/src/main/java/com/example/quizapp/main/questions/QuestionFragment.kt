package com.example.quizapp.main.questions

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.quizapp.R
import com.example.quizapp.`interface`.InterfaceAnswerSelect
import com.example.quizapp.common.Common
import com.example.quizapp.databinding.FragmentQuestionBinding
import com.example.quizapp.model.CurrentQuestion
import com.example.quizapp.model.Question
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class QuestionFragment : Fragment(), InterfaceAnswerSelect {

    private lateinit var binding : FragmentQuestionBinding
    private var question : Question? = null
    private var questionIndex = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_question, container, false)

        questionIndex = arguments!!.getInt("index", -1)
        question = Common.questionList[questionIndex]
        if (question != null) {
            if (question!!.isImageQuestion) {
                Picasso.get()
                        .load(question!!.questionImage)
                        .into(binding.ivQuestion, object : Callback {
                            override fun onSuccess() {
                                binding.progressBar.visibility = View.GONE
                            }

                            override fun onError(e: Exception?) {
                                binding.ivQuestion.setImageResource(R.drawable.ic_error)
                            }

                        })
            } else {
                binding.progressBar.visibility = View.GONE
                binding.ivQuestion.visibility = View.GONE
            }

            binding.tvQuestion.text = question!!.questionText
            binding.ckbAnswerA.text = question!!.answerA
            binding.ckbAnswerB.text = question!!.answerB
            binding.ckbAnswerC.text = question!!.answerC
            binding.ckbAnswerD.text = question!!.answerD

            binding.ckbAnswerA.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    Common.selectedValue.add(binding.ckbAnswerA.toString())
                } else {
                    Common.selectedValue.remove(binding.ckbAnswerA.toString())
                }
            }

            binding.ckbAnswerB.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    Common.selectedValue.add(binding.ckbAnswerB.toString())
                } else {
                    Common.selectedValue.remove(binding.ckbAnswerB.toString())
                }
            }

            binding.ckbAnswerC.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    Common.selectedValue.add(binding.ckbAnswerC.toString())
                } else {
                    Common.selectedValue.remove(binding.ckbAnswerC.toString())
                }
            }

            binding.ckbAnswerD.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    Common.selectedValue.add(binding.ckbAnswerD.toString())
                } else {
                    Common.selectedValue.remove(binding.ckbAnswerD.toString())
                }
            }

        }

        return binding.root
    }

    override fun selectedAnswer(): CurrentQuestion {
        Common.selectedValue.distinct()
        Common.selectedValue.sort()

        if (Common.answerSheetList[questionIndex].type == Common.ANSWER_TYPE.NO_ANSWER) {
            val currentQuestion = CurrentQuestion(questionIndex, Common.ANSWER_TYPE.NO_ANSWER)
            val result = StringBuilder()
            if (Common.selectedValue.size > 1) {
                val arrayAnswer = Common.selectedValue.toTypedArray()
                for (i in arrayAnswer.indices) {
                    if (i < arrayAnswer.size-1) {
                        result.append(StringBuilder((arrayAnswer[i]).substring(0,1)).append(","))
                    } else {
                        result.append((arrayAnswer[i]).substring(0,1))
                    }
                }
            } else if (Common.selectedValue.size == 1) {
                val arrayAnswer = Common.selectedValue.toTypedArray()
                result.append((arrayAnswer[0]).substring(0,1))
            }

            if (question != null) {
                if (result.isNotEmpty()) {
                    Log.d("result:", result.toString())
                    Log.d("correctAnswer:", question!!.correctAnswer.toString())
                    if(result.toString() == question!!.correctAnswer) {
                        currentQuestion.type = Common.ANSWER_TYPE.RIGHT_ANSWER
                    } else {
                        currentQuestion.type = Common.ANSWER_TYPE.WRONG_ANSWER
                    }
                } else {
                    currentQuestion.type = Common.ANSWER_TYPE.NO_ANSWER
                }
            } else {
                Toast.makeText(activity, "Ooppss\nCannot get question....", Toast.LENGTH_SHORT).show()
                currentQuestion.type = Common.ANSWER_TYPE.NO_ANSWER
            }

            Common.selectedValue.clear()
            return currentQuestion

        } else {
            return Common.answerSheetList[questionIndex]
        }
    }

    override fun showCorrectAnswer() {
        val correctAnswer = question!!.correctAnswer!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }
        for (answer in correctAnswer) {
            if (answer.equals("A", true)) {
                binding.ckbAnswerA.setTypeface(null, Typeface.BOLD)
                binding.ckbAnswerA.setTextColor(Color.BLACK)
            } else if (answer.equals("B", true)) {
                binding.ckbAnswerB.setTypeface(null, Typeface.BOLD)
                binding.ckbAnswerB.setTextColor(Color.BLACK)
            } else if (answer.equals("C", true)) {
                binding.ckbAnswerC.setTypeface(null, Typeface.BOLD)
                binding.ckbAnswerC.setTextColor(Color.BLACK)
            } else if (answer.equals("D", true)) {
                binding.ckbAnswerD.setTypeface(null, Typeface.BOLD)
                binding.ckbAnswerD.setTextColor(Color.BLACK)
            }
        }
    }

    override fun disableAnswer() {
        binding.ckbAnswerA.isEnabled = false
        binding.ckbAnswerB.isEnabled = false
        binding.ckbAnswerC.isEnabled = false
        binding.ckbAnswerD.isEnabled = false

    }

    override fun resetQuestion() {
        binding.ckbAnswerA.isEnabled = true
        binding.ckbAnswerB.isEnabled = true
        binding.ckbAnswerC.isEnabled = true
        binding.ckbAnswerD.isEnabled = true

        binding.ckbAnswerA.isChecked = false
        binding.ckbAnswerB.isChecked = false
        binding.ckbAnswerC.isChecked = false
        binding.ckbAnswerD.isChecked = false

        binding.ckbAnswerA.setTypeface(null, Typeface.NORMAL)
        binding.ckbAnswerB.setTypeface(null, Typeface.NORMAL)
        binding.ckbAnswerC.setTypeface(null, Typeface.NORMAL)
        binding.ckbAnswerD.setTypeface(null, Typeface.NORMAL)

        Common.selectedValue.clear()

    }

}