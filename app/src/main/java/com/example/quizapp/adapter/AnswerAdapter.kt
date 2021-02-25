package com.example.quizapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import com.example.quizapp.common.Common
import com.example.quizapp.databinding.ItemGridQuestionBinding
import com.example.quizapp.model.CurrentQuestion

class AnswerAdapter(var context: Context, private var answerSheetList : List<CurrentQuestion>) : RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder>(){

    inner class AnswerViewHolder(val binding: ItemGridQuestionBinding) : RecyclerView.ViewHolder(
            binding.root
    ) {
        fun bind(answerSheetList: List<CurrentQuestion>, position: Int) {
            when (answerSheetList[position].type) {
                Common.ANSWER_TYPE.RIGHT_ANSWER -> {
                    binding.viewQuestionItem.setBackgroundResource(R.drawable.shape_question_answered)
                }
                Common.ANSWER_TYPE.WRONG_ANSWER -> {
                    binding.viewQuestionItem.setBackgroundResource(R.drawable.shape_question_answered)
                }
                else -> {
                    binding.viewQuestionItem.setBackgroundResource(R.drawable.shape_question_no_answer)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        return AnswerViewHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_grid_question,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.bind(answerSheetList, position)
    }

    override fun getItemCount(): Int = answerSheetList.size


}