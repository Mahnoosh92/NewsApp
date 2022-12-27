package com.mahdavi.newsapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mahdavi.newsapp.data.model.local.Source
import com.mahdavi.newsapp.data.model.local.entity.SearchedArticleEntity
import com.mahdavi.newsapp.data.model.local.entity.SourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SourceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(source: SourceEntity)

    @Query("select * from sources")
    fun getSources(): Flow<List<SourceEntity>>

    @Query("DELETE FROM sources")
    suspend fun clear()
}