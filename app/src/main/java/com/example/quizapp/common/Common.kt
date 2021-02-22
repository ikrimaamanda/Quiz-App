package com.example.quizapp.common

import com.example.quizapp.main.questions.QuestionFragment
import com.example.quizapp.model.Category
import com.example.quizapp.model.CurrentQuestion
import com.example.quizapp.model.Question

object Common {

    const val TOTAL_TIME = 5*60*1000
    var answerSheetList : MutableList<CurrentQuestion> = ArrayList()
    var questionList : MutableList<Question> = ArrayList()
    var selectedCategory : Category? = null

    var fragmentList : MutableList<QuestionFragment> = ArrayList()
    var selectedValue : MutableList<String> = ArrayList()


    enum class ANSWER_TYPE {
        NO_ANSWER,
        RIGHT_ANSWER,
        WRONG_ANSWER
    }
}