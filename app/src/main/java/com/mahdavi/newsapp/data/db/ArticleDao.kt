package com.mahdavi.newsapp.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mahdavi.newsapp.data.model.local.entity.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<Article>)

    @Query("select * from articles")
    fun getArticles(): Flow<List<Article>>

    @Query("DELETE FROM articles")
    suspend fun clear()
}