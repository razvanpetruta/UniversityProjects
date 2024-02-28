package com.example.androiddbserver.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_reports")
data class DailyReportEntity(
    @PrimaryKey()
    val id: Int,
    val title: String,
    val gratitude: String,
    val accomplishments: String,
    val shortcomings: String,
    val improvementAreas: String,
    val rating: Int,
    val date: String,
    val needsSync: Boolean
)