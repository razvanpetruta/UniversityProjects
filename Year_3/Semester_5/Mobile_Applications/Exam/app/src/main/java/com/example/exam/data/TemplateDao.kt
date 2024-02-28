package com.example.exam.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TemplateDao {
    @Query("SELECT * FROM listings")
    fun getTemplates(): Flow<List<ListingEntity>>

    @Query("SELECT * FROM listings WHERE id = :id")
    fun getTemplate(id: Int): Flow<ListingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTemplate(listingEntity: ListingEntity)

    @Query("DELETE FROM listings")
    suspend fun deleteAllTemplates()
}