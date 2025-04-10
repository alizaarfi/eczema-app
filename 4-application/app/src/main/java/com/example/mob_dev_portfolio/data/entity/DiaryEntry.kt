package com.example.mob_dev_portfolio.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary_entries")
data class DiaryEntry(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var foodName: String? = null,
    var skincareName: String? = null,
    var reactionSeverity: String? = null,
    var date: String? = null
)