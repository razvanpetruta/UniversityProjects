package com.example.androiddbserver.data.mapper

import com.example.androiddbserver.data.local.entity.DailyReportEntity
import com.example.androiddbserver.domain.model.DailyReport

fun DailyReportEntity.asExternalModel(): DailyReport = DailyReport(
    id, title, gratitude, accomplishments, shortcomings, improvementAreas, rating, date, needsSync
)

fun DailyReport.toEntity(): DailyReportEntity = DailyReportEntity(
    id, title, gratitude, accomplishments, shortcomings, improvementAreas, rating, date, needsSync
)