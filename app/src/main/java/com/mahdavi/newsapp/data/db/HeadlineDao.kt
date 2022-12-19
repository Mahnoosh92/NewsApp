package com.mahdavi.newsapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mahdavi.newsapp.data.model.local.entity.HeadlineArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HeadlineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<HeadlineArticleEntity>)

    @Query("select * from headline_articles")
    fun getArticles(): Flow<List<HeadlineArticleEntity>>

    @Query("DELETE FROM headline_articles")
    suspend fun clear()
}