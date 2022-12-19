package com.mahdavi.newsapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mahdavi.newsapp.data.model.local.entity.HeadlineArticleEntity
import com.mahdavi.newsapp.data.model.local.entity.SearchedArticleEntity


@Database(
    entities = [HeadlineArticleEntity::class, SearchedArticleEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun headlineArticleDao(): HeadlineDao
    abstract fun searchedArticleDao(): SearchDao
}