package com.mahdavi.newsapp.data.model.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mahdavi.newsapp.data.model.local.Source


@Entity(tableName = "sources")
data class SourceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "source") val source: String?
) {

    fun toSource() = Source(id = this.id, source = this.source ?: "")
}
