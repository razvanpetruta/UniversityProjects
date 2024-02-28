package com.example.androiddb.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.androiddb.data.local.dao.DailyReportDao
import com.example.androiddb.data.local.entity.DailyReportEntity
import com.example.androiddb.utils.DateConverter

@Database(
    version = 2,
    entities = [DailyReportEntity::class]
)
@TypeConverters(DateConverter::class)
abstract class DailyReportDatabase : RoomDatabase() {
    abstract val dao: DailyReportDao

    companion object {
        const val name = "daily_reports_db"
    }
}