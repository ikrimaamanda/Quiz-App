package com.example.quizapp.model

data class QuestionRecyclerView(var id: Int?,
                                var questionText : String?,
                                var questionImage : String?,
                                var answerA : String?,
                                var answerB : String?,
                                var answerC : String?,
                                var answerD : String?,
                                var correctAnswer : String?,
                                var isImageQuestion : Boolean,
                                var categoryId : Int?,
                                var expandable : Boolean = false
)
