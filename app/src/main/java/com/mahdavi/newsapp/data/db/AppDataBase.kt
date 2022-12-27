package com.mahdavi.newsapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mahdavi.newsapp.data.model.local.entity.FavouriteHeadLineArticleEntity
import com.mahdavi.newsapp.data.model.local.entity.HeadlineArticleEntity
import com.mahdavi.newsapp.data.model.local.entity.SearchedArticleEntity
import com.mahdavi.newsapp.data.model.local.entity.SourceEntity


@Database(
    entities = [HeadlineArticleEntity::class, SearchedArticleEntity::class, SourceEntity::class, FavouriteHeadLineArticleEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun headlineArticleDao(): HeadlineDao
    abstract fun searchedArticleDao(): SearchDao
    abstract fun sourceDao(): SourceDao
    abstract fun favouriteHeadlineDao(): FavouriteHeadlineDao
}