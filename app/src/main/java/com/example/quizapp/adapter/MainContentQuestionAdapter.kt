package com.example.quizapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.quizapp.main.questions.QuestionFragment

class MainContentQuestionAdapter(fragment: FragmentManager, private var fragmentList: List<QuestionFragment>) : FragmentStatePagerAdapter(fragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = fragmentList.size

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return StringBuilder("Question ").append(position+1).toString()
    }

}