package com.example.quizapp.main.achievement

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import com.example.quizapp.common.Common
import com.example.quizapp.databinding.ItemQuestionOfAchievementBinding
import com.example.quizapp.model.QuestionRecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class AchievementAdapter(private var questionList: List<QuestionRecyclerView>) : RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder>(){

    inner class AchievementViewHolder(val binding : ItemQuestionOfAchievementBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        return AchievementViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_question_of_achievement,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        val items = questionList[position]

        if (items.isImageQuestion) {
            Picasso
                .get()
                .load(items.questionImage)
                .into(holder.binding.ivQuestion, object : Callback {
                    override fun onSuccess() {
                        holder.binding.progressBar.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        holder.binding.ivQuestion.setImageResource(R.drawable.ic_error)
                    }

                })
        } else {
            holder.binding.progressBar.visibility = View.GONE
            holder.binding.ivQuestion.visibility = View.GONE
        }

        holder.binding.tvTitle.text = "Question ${position+1}"
        holder.binding.tvQuestionText.text = items.questionText
        holder.binding.ckbAnswerA.text = items.answerA
        holder.binding.ckbAnswerB.text = items.answerB
        holder.binding.ckbAnswerC.text = items.answerC
        holder.binding.ckbAnswerD.text = items.answerD

        val isExpandable : Boolean = questionList[position].expandable

        if (isExpandable) {
            holder.binding.expandableLayout.visibility = View.VISIBLE
            holder.binding.ivArrow.visibility = View.GONE
            holder.binding.ivArrowDown.visibility = View.VISIBLE

            val correctAnswers = items.correctAnswer!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }

            for (answer in correctAnswers) {
                when {
                    answer.equals("A", true) -> {
                        holder.binding.ckbAnswerA.setTypeface(null, Typeface.BOLD)
                        holder.binding.ckbAnswerA.setTextColor(Color.BLACK)
                    }
                    answer.equals("B", true) -> {
                        holder.binding.ckbAnswerB.setTypeface(null, Typeface.BOLD)
                        holder.binding.ckbAnswerB.setTextColor(Color.BLACK)
                    }
                    answer.equals("C", true) -> {
                        holder.binding.ckbAnswerC.setTypeface(null, Typeface.BOLD)
                        holder.binding.ckbAnswerC.setTextColor(Color.BLACK)
                    }
                    answer.equals("D", true) -> {
                        holder.binding.ckbAnswerD.setTypeface(null, Typeface.BOLD)
                        holder.binding.ckbAnswerD.setTextColor(Color.BLACK)
                    }
                }
            }

//            if (Common.answerSheetList[position].type == Common.ANSWER_TYPE.NO_ANSWER) {
//
//            }

            holder.binding.ckbAnswerA.isEnabled = false
            holder.binding.ckbAnswerB.isEnabled = false
            holder.binding.ckbAnswerC.isEnabled = false
            holder.binding.ckbAnswerD.isEnabled = false

            Common.selectedValue.clear()

        } else {
            holder.binding.expandableLayout.visibility = View.GONE
            holder.binding.ivArrowDown.visibility = View.GONE

        }

        holder.binding.linearLayout.setOnClickListener {
            val questions = questionList[position]
            questions.expandable = !questions.expandable
            notifyItemChanged(position)
            holder.binding.ckbAnswerA.setTypeface(null, Typeface.NORMAL)
            holder.binding.ckbAnswerA.setTextColor(Color.GRAY)
            holder.binding.ckbAnswerB.setTypeface(null, Typeface.NORMAL)
            holder.binding.ckbAnswerB.setTextColor(Color.GRAY)
            holder.binding.ckbAnswerC.setTypeface(null, Typeface.NORMAL)
            holder.binding.ckbAnswerC.setTextColor(Color.GRAY)
            holder.binding.ckbAnswerD.setTypeface(null, Typeface.NORMAL)
            holder.binding.ckbAnswerD.setTextColor(Color.GRAY)
        }
    }

    override fun getItemCount(): Int = questionList.size

}