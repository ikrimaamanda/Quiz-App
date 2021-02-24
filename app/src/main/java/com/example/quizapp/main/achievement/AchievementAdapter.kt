package com.example.quizapp.main.achievement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
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
        holder.binding.expandableLayout.visibility = if (isExpandable) View.VISIBLE else View.GONE

        if (isExpandable) {
            holder.binding.expandableLayout.visibility = View.VISIBLE
            holder.binding.ivArrow.visibility = View.GONE
            holder.binding.ivArrowDown.visibility = View.VISIBLE
        } else {
            holder.binding.expandableLayout.visibility = View.GONE
            holder.binding.ivArrowDown.visibility = View.GONE
        }

        holder.binding.linearLayout.setOnClickListener {
            val question = questionList[position]
            question.expandable = !question.expandable
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = questionList.size
}