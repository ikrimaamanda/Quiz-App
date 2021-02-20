package com.example.quizapp.dbhelper

import android.content.Context
import android.database.Cursor
import com.example.quizapp.model.Category
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper


class DBHelper(context: Context) : SQLiteAssetHelper(context, "EurekaQuiz2021.db", null, 1) {

    fun getAllCategories(context: Context) : List<Category> {
        val helper = DBHelper(context)
        val db = helper.writableDatabase

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
}