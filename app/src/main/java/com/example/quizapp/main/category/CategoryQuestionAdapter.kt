package com.example.quizapp.main.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import com.example.quizapp.databinding.ItemCategoryOfQuestionsBinding
import com.example.quizapp.model.Category

class CategoryQuestionAdapter(val categories : ArrayList<Category>) : RecyclerView.Adapter<CategoryQuestionAdapter.CategoryViewHolder>() {

    private lateinit var onItemCategoryClickCallback: OnItemCategoryClickCallBack

    fun onItemCategoryClicked(onItemCategoryClickCallback: OnItemCategoryClickCallBack) {
        this.onItemCategoryClickCallback = onItemCategoryClickCallback
    }

    interface OnItemCategoryClickCallBack {
        fun onItemCategoryClicked(categoryModel: Category)
    }

    inner class CategoryViewHolder(val binding: ItemCategoryOfQuestionsBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(categoryModel: Category) {
            binding.model = categoryModel
            binding.tvTitleQuestion.setOnClickListener {
                onItemCategoryClickCallback.onItemCategoryClicked(categories[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_category_of_questions,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size
}