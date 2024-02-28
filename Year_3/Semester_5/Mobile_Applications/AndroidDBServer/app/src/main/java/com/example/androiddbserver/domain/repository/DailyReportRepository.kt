package com.example.androiddbserver.domain.repository

import com.example.androiddbserver.domain.model.DailyReport
import kotlinx.coroutines.flow.Flow

interface DailyReportRepository {
    fun getAllDailyReports(): Flow<List<DailyReport>>

    fun getDailyReportById(id: Int): Flow<DailyReport>

    fun countReportById(id: Int): Int

    suspend fun addDailyReport(dailyReport: DailyReport)

    suspend fun updateDailyReport(dailyReport: DailyReport)

    suspend fun deleteDailyReport(dailyReport: DailyReport)

    suspend fun deleteDailyReportById(id: Int)

    suspend fun deleteAllReports()
}