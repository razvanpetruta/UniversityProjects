package com.example.dailyreportapp.data

import java.util.Date
import java.util.UUID

class DailyReport(
    var id: UUID,
    var title: String,
    var gratitude: String,
    var accomplishments: String,
    var shortcomings: String,
    var improvementAreas: String,
    var rating: Int,
    var date: Date
)