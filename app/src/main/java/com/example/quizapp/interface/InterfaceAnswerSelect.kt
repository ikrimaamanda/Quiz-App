package com.example.quizapp.`interface`

import com.example.quizapp.model.CurrentQuestion

interface InterfaceAnswerSelect {
    fun selectedAnswer() : CurrentQuestion
    fun showCorrectAnswer()
    fun disableAnswer()
    fun resetQuestion()
}