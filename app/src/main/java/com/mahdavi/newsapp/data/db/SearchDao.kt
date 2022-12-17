package com.mahdavi.newsapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mahdavi.newsapp.data.model.local.entity.SearchedNewsArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<SearchedNewsArticleEntity>)

    @Query("select * from searched_articles")
    fun getArticles(): Flow<List<SearchedNewsArticleEntity>>

    @Query("DELETE FROM searched_articles")
    suspend fun clear()
}