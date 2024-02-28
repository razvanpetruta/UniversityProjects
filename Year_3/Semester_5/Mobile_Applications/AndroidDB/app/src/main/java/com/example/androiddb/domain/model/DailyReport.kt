package com.example.androiddb.domain.model

import java.util.Date
import java.util.UUID

data class DailyReport(
    val id: UUID,
    val title: String,
    val gratitude: String,
    val accomplishments: String,
    val shortcomings: String,
    val improvementAreas: String,
    val rating: Int,
    val date: Date
)