package com.example.quizapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.quizapp.main.questions.No1Fragment

class TabPagerAdapter(fragment : FragmentManager) : FragmentStatePagerAdapter(fragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragment = arrayOf(
        No1Fragment()
    )

    override fun getCount(): Int = fragment.size

    override fun getItem(position: Int): Fragment {
        return fragment[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when(position) {
            0 -> "Question 1"
            1 -> "Question 2"
            2 -> "Question 3"
            3 -> "Question 4"
            4 -> "Question 5"
            else -> ""
        }
    }

}