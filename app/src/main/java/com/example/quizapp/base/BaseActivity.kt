package com.example.quizapp.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<ActivityBinding : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var binding : ActivityBinding
    protected var setLayout = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, setLayout)
    }

    protected inline fun <reified ActivityClass> intent(context: Context) {
        context.startActivity(Intent(context, ActivityClass::class.java))
    }
}