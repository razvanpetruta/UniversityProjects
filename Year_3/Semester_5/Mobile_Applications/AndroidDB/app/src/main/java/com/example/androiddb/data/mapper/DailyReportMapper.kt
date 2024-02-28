package com.example.androiddb.data.mapper

import com.example.androiddb.data.local.entity.DailyReportEntity
import com.example.androiddb.domain.model.DailyReport

fun DailyReportEntity.asExternalModel(): DailyReport = DailyReport(
    id, title, gratitude, accomplishments, shortcomings, improvementAreas, rating, date
)

fun DailyReport.toEntity(): DailyReportEntity = DailyReportEntity(
    id, title, gratitude, accomplishments, shortcomings, improvementAreas, rating, date
)