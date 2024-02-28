package com.example.androiddb.domain.repository

import com.example.androiddb.domain.model.DailyReport
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface DailyReportRepository {
    fun getAllDailyReports(): Flow<List<DailyReport>>

    fun getDailyReportById(id: UUID): Flow<DailyReport>

    suspend fun addDailyReport(dailyReport: DailyReport)

    suspend fun updateDailyReport(dailyReport: DailyReport)

    suspend fun deleteDailyReport(dailyReport: DailyReport)
}