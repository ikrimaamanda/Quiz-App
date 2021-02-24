package com.example.quizapp.main.achievement

import android.os.Bundle
import com.example.quizapp.R
import com.example.quizapp.base.BaseActivity
import com.example.quizapp.databinding.ActivityAchievementBinding

class AchievementActivity : BaseActivity<ActivityAchievementBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_achievement
        super.onCreate(savedInstanceState)
    }
}