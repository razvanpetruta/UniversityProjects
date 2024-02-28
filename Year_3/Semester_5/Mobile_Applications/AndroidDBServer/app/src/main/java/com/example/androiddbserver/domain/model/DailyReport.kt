package com.example.androiddbserver.domain.model

data class DailyReport(
    var id: Int,
    val title: String,
    val gratitude: String,
    val accomplishments: String,
    val shortcomings: String,
    val improvementAreas: String,
    val rating: Int,
    val date: String,
    var needsSync: Boolean = false
)