package com.example.androiddb.data.repository

import com.example.androiddb.data.local.dao.DailyReportDao
import com.example.androiddb.data.mapper.asExternalModel
import com.example.androiddb.data.mapper.toEntity
import com.example.androiddb.domain.model.DailyReport
import com.example.androiddb.domain.repository.DailyReportRepository
import com.example.androiddb.utils.DataOperationException
import com.example.androiddb.utils.logd
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class DailyReportRepositoryImpl(
    private val dao: DailyReportDao
) : DailyReportRepository {
    override fun getAllDailyReports(): Flow<List<DailyReport>> {
        return dao.getAllDailyReports()
            .map { dailyReports ->
                dailyReports.map {
                    it.asExternalModel()
                }
            }
    }

    override fun getDailyReportById(id: UUID): Flow<DailyReport> {
        return dao.getDailyReportById(id)
            .map {
                it!!.asExternalModel()
            }
    }

    override suspend fun addDailyReport(dailyReport: DailyReport) {
        try {
            delay(2000)
            dao.addDailyReport(dailyReport.toEntity())
            logd("${dailyReport.title} added successfully")
        } catch (e: Exception) {
            logd("Could not add ${dailyReport.title}")
            throw DataOperationException("Failed to add daily report", e)
        }
    }

    override suspend fun updateDailyReport(dailyReport: DailyReport) {
        try {
            delay(2000)
            dao.updateDailyReport(dailyReport.toEntity())
            logd("${dailyReport.title} updated successfully")
        } catch (e: Exception) {
            logd("Could not update ${dailyReport.title}")
            throw DataOperationException("Failed to update daily report", e)
        }
    }

    override suspend fun deleteDailyReport(dailyReport: DailyReport) {
        try {
            delay(2000)
            dao.deleteDailyReport(dailyReport.toEntity())
            logd("${dailyReport.title} deleted successfully")
        } catch (e: Exception) {
            logd("Could not delete ${dailyReport.title}")
            throw DataOperationException("Failed to delete daily report", e)
        }
    }
}