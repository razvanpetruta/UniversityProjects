package com.example.androiddb.data.local.dao;

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.androiddb.data.local.entity.DailyReportEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface DailyReportDao {
    @Query("SELECT * FROM daily_reports")
    fun getAllDailyReports(): Flow<List<DailyReportEntity>>

    @Query("""
        SELECT * FROM daily_reports
        WHERE id = :id
    """)
    fun getDailyReportById(id: UUID): Flow<DailyReportEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDailyReport(dailyReportEntity: DailyReportEntity)

    @Update
    suspend fun updateDailyReport(dailyReportEntity: DailyReportEntity)

    @Delete
    suspend fun deleteDailyReport(dailyReportEntity: DailyReportEntity)
}
