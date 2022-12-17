package com.mahdavi.newsapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mahdavi.newsapp.data.model.local.entity.NewsHeadlineArticleEntity
import com.mahdavi.newsapp.data.model.local.entity.SearchedNewsArticleEntity


@Database(
    entities = [NewsHeadlineArticleEntity::class, SearchedNewsArticleEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun headlineArticleDao(): HeadlineDao
    abstract fun searchedArticleDao(): SearchDao
}