package com.example.quizapp.main.category

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quizapp.R
import com.example.quizapp.base.BaseActivity
import com.example.quizapp.common.Common
import com.example.quizapp.databinding.ActivityCategoryQuestionBinding
import com.example.quizapp.dbhelper.DBHelper
import com.example.quizapp.main.questions.MainContentQuestionActivity
import com.example.quizapp.model.Category

class CategoryQuestionActivity : BaseActivity<ActivityCategoryQuestionBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setLayout = R.layout.activity_category_question
        super.onCreate(savedInstanceState)

        setRecyclerView()

    }

    private fun setRecyclerView() {
        val rvAdapter = CategoryQuestionAdapter(
            DBHelper(this).getAllCategories(this) as ArrayList<Category>
        )

        binding.rvCategory.apply {
            binding.rvCategory.layoutManager = GridLayoutManager(this@CategoryQuestionActivity, 3)
            rvAdapter.onItemCategoryClicked(object : CategoryQuestionAdapter.OnItemCategoryClickCallBack {
                override fun onItemCategoryClicked(categoryModel: Category) {
                    Common.selectedCategory = categoryModel
                    Toast.makeText(this@CategoryQuestionActivity, "${categoryModel.name} clicked", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@CategoryQuestionActivity, MainContentQuestionActivity::class.java))
                }
            })
            adapter = rvAdapter
        }
    }
}