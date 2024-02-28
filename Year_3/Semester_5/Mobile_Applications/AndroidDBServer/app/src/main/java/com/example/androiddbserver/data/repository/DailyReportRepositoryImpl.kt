package com.example.androiddbserver.data.repository

import com.example.androiddbserver.data.local.dao.DailyReportDao
import com.example.androiddbserver.data.mapper.asExternalModel
import com.example.androiddbserver.data.mapper.toEntity
import com.example.androiddbserver.domain.model.DailyReport
import com.example.androiddbserver.domain.repository.DailyReportRepository
import com.example.androiddbserver.utils.DataOperationException
import com.example.androiddbserver.utils.logd
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    override fun getDailyReportById(id: Int): Flow<DailyReport> {
        return dao.getDailyReportById(id)
            .map {
                it!!.asExternalModel()
            }
    }

    override fun countReportById(id: Int): Int {
        return dao.countReportById(id)
    }

    override suspend fun addDailyReport(dailyReport: DailyReport) {
        try {
            logd("Local DB: Performing add operation")
            dao.addDailyReport(dailyReport.toEntity())
            logd("Local DB: ${dailyReport.title} added successfully")
        } catch (e: Exception) {
            logd("Local DB: Could not add ${dailyReport.title}")
            throw DataOperationException("Failed to add daily report", e)
        }
    }

    override suspend fun updateDailyReport(dailyReport: DailyReport) {
        try {
            logd("Local DB: Performing update operation")
            dao.updateDailyReport(dailyReport.toEntity())
            logd("Local DB: ${dailyReport.title} updated successfully")
        } catch (e: Exception) {
            logd("Local DB: Could not update ${dailyReport.title}")
            throw DataOperationException("Failed to update daily report", e)
        }
    }

    override suspend fun deleteDailyReport(dailyReport: DailyReport) {
        try {
            logd("Local DB: Performing delete operation")
            dao.deleteDailyReport(dailyReport.toEntity())
            logd("Local DB: ${dailyReport.title} deleted successfully")
        } catch (e: Exception) {
            logd("Local DB: Could not delete ${dailyReport.title}")
            throw DataOperationException("Failed to delete daily report", e)
        }
    }

    override suspend fun deleteDailyReportById(id: Int) {
        try {
            logd("Local DB: Performing delete operation")
            dao.deleteDailyReportById(id)
            logd("Local DB: Report with id $id deleted successfully")
        } catch (e: Exception) {
            logd("Local DB: Could not delete report with id $id")
            throw DataOperationException("Failed to delete daily report", e)
        }
    }

    override suspend fun deleteAllReports() {
        try {
            logd("Local DB: Performing delete operation")
            dao.deleteAllReports()
            logd("Local DB: All reports deleted successfully")
        } catch (e: Exception) {
            logd("Local DB: Could not delete all reports")
            throw DataOperationException("Failed to delete all daily reports", e)
        }
    }
}