package com.mahdavi.newsapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mahdavi.newsapp.data.model.local.entity.FavouriteHeadLineArticleEntity
import com.mahdavi.newsapp.data.model.local.entity.HeadlineArticleEntity
import com.mahdavi.newsapp.data.model.remote.news.HeadlineArticle
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteHeadlineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(headline: FavouriteHeadLineArticleEntity)

    @Query("select * from favourite_headlines")
    fun getFavouriteHeadlines(): Flow<List<FavouriteHeadLineArticleEntity>>

    @Query("DELETE FROM favourite_headlines")
    suspend fun clear()

}