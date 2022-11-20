package com.mahdavi.newsapp.data.db

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mahdavi.newsapp.data.model.local.entity.Article


@Database(entities = [Article::class], version = 1, exportSchema = false)
abstract class AppDataBase() : RoomDatabase() {

    abstract fun articleDao(): ArticleDao


}