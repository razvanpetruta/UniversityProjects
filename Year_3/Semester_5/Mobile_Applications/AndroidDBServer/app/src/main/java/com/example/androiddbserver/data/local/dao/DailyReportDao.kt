package com.example.androiddbserver.data.local.dao;

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.androiddbserver.data.local.entity.DailyReportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyReportDao {
    @Query("SELECT * FROM daily_reports")
    fun getAllDailyReports(): Flow<List<DailyReportEntity>>

    @Query("""
        SELECT * FROM daily_reports
        WHERE id = :id
    """)
    fun getDailyReportById(id: Int): Flow<DailyReportEntity?>

    @Query("SELECT COUNT(*) FROM daily_reports WHERE id = :id")
    fun countReportById(id: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDailyReport(dailyReportEntity: DailyReportEntity)

    @Update
    suspend fun updateDailyReport(dailyReportEntity: DailyReportEntity)

    @Delete
    suspend fun deleteDailyReport(dailyReportEntity: DailyReportEntity)

    @Query("DELETE FROM daily_reports WHERE id = :id")
    suspend fun deleteDailyReportById(id: Int)

    @Query("DELETE FROM daily_reports")
    suspend fun deleteAllReports()
}
