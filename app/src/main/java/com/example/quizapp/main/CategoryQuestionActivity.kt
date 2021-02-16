package com.example.quizapp.main

import android.os.Bundle
import com.example.quizapp.R
import com.example.quizapp.base.BaseActivity
import com.example.quizapp.databinding.ActivityCategoryQuestionBinding

class CategoryQuestionActivity : BaseActivity<ActivityCategoryQuestionBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_category_question
        super.onCreate(savedInstanceState)
    }
}