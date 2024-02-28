package com.example.androiddb.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "daily_reports")
data class DailyReportEntity(
    @PrimaryKey
    val id: UUID,
    val title: String,
    val gratitude: String,
    val accomplishments: String,
    val shortcomings: String,
    val improvementAreas: String,
    val rating: Int,
    val date: Date
)