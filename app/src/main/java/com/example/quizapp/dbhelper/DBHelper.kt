package com.example.quizapp.dbhelper

import android.content.Context
import android.database.Cursor
import com.example.quizapp.model.Category
import com.example.quizapp.model.Question
import com.example.quizapp.model.QuestionRecyclerView
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper

class DBHelper(context: Context) : SQLiteAssetHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private var instance : DBHelper? = null
        private const val DB_NAME = "QuizApps.db"
        private const val DB_VERSION = 1

        @Synchronized
        fun getInstance(context: Context) : DBHelper {
            if (instance == null) {
                instance = DBHelper(context)
            }
            return instance!!
        }
    }

    fun getAllCategories(context: Context) : List<Category> {
        getInstance(context)
        val db = instance!!.writableDatabase

        val cursor: Cursor = db.rawQuery("SELECT * FROM Category;", null)

        val categories: MutableList<Category> = ArrayList()

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val category = Category(cursor.getInt(cursor.getColumnIndex("ID")),
                    cursor.getString(cursor.getColumnIndex("Name")),
                    cursor.getString(cursor.getColumnIndex("Image")))

                categories.add(category)
                cursor.moveToNext()
            }
        }

        cursor.close()
        db.close()

        return categories
    }

    fun getQuestionByCategory(context: Context, categoryId : Int) : MutableList<Question> {
        getInstance(context)
        val db = instance!!.writableDatabase

        val cursor: Cursor = db.rawQuery("SELECT * FROM Question WHERE categoryId=$categoryId;", null)

        val questions: MutableList<Question> = ArrayList()

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val question = Question(cursor.getInt(cursor.getColumnIndex("ID")),
                        cursor.getString(cursor.getColumnIndex("QuestionText")),
                        cursor.getString(cursor.getColumnIndex("QuestionImage")),
                        cursor.getString(cursor.getColumnIndex("AnswerA")),
                        cursor.getString(cursor.getColumnIndex("AnswerB")),
                        cursor.getString(cursor.getColumnIndex("AnswerC")),
                        cursor.getString(cursor.getColumnIndex("AnswerD")),
                        cursor.getString(cursor.getColumnIndex("CorrectAnswer")),
                        if (cursor.getInt(cursor.getColumnIndex("IsImageQuestion")) == 0) java.lang.Boolean.FALSE else java.lang.Boolean.TRUE,
                        cursor.getInt(cursor.getColumnIndex("CategoryID"))
                )

                questions.add(question)
                cursor.moveToNext()
            }
        }

        cursor.close()
        db.close()

        return questions
    }

    fun getQuestionAchievementByCategory(context: Context, categoryId : Int) : MutableList<QuestionRecyclerView> {
        getInstance(context)
        val db = instance!!.writableDatabase

        val cursor: Cursor = db.rawQuery("SELECT * FROM Question WHERE categoryId=$categoryId;", null)

        val questions: MutableList<QuestionRecyclerView> = ArrayList()

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val question = QuestionRecyclerView(cursor.getInt(cursor.getColumnIndex("ID")),
                    cursor.getString(cursor.getColumnIndex("QuestionText")),
                    cursor.getString(cursor.getColumnIndex("QuestionImage")),
                    cursor.getString(cursor.getColumnIndex("AnswerA")),
                    cursor.getString(cursor.getColumnIndex("AnswerB")),
                    cursor.getString(cursor.getColumnIndex("AnswerC")),
                    cursor.getString(cursor.getColumnIndex("AnswerD")),
                    cursor.getString(cursor.getColumnIndex("CorrectAnswer")),
                    if (cursor.getInt(cursor.getColumnIndex("IsImageQuestion")) == 0) java.lang.Boolean.FALSE else java.lang.Boolean.TRUE,
                    cursor.getInt(cursor.getColumnIndex("CategoryID"))
                )

                questions.add(question)
                cursor.moveToNext()
            }
        }

        cursor.close()
        db.close()

        return questions
    }

}