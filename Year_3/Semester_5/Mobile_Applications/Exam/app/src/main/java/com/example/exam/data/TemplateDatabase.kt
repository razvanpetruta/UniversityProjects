package com.example.exam.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [ListingEntity::class]
)
abstract class TemplateDatabase : RoomDatabase() {
    abstract val dao: TemplateDao

    companion object {
        const val name = "template_db"
    }
}