package com.example.quizapp.main.category

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.quizapp.R
import com.example.quizapp.base.BaseActivity
import com.example.quizapp.databinding.ActivityCategoryQuestionBinding
import com.example.quizapp.dbhelper.DBHelper
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
            binding.rvCategory.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            rvAdapter.onItemCategoryClicked(object : CategoryQuestionAdapter.OnItemCategoryClickCallBack {
                override fun onItemCategoryClicked(categoryModel: Category) {
                    Toast.makeText(this@CategoryQuestionActivity, "${categoryModel.name} clicked", Toast.LENGTH_SHORT).show()
                }
            })
            adapter = rvAdapter
        }
    }
}