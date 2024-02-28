package com.example.androiddbserver.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androiddbserver.data.local.dao.DailyReportDao
import com.example.androiddbserver.data.local.entity.DailyReportEntity

@Database(
    version = 1,
    entities = [DailyReportEntity::class]
)
abstract class DailyReportDatabase : RoomDatabase() {
    abstract val dao: DailyReportDao

    companion object {
        const val name = "daily_reports_db"
    }
}