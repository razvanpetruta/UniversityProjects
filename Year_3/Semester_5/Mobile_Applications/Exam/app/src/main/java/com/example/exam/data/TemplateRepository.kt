package com.example.exam.data

import com.example.exam.logd
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface TemplateRepository {
    fun getListings(): Flow<List<Listing>>
    fun getListing(id: Int): Flow<Listing>
    suspend fun addListing(listing: Listing)
    suspend fun deleteAllListings()
}

class TemplateRepositoryImpl(
    private val dao: TemplateDao
) : TemplateRepository {
    override fun getListings(): Flow<List<Listing>> =
        dao.getTemplates().map {
            it.map(ListingEntity::asExternalModel)
        }

    override fun getListing(id: Int): Flow<Listing> =
        dao.getTemplate(id).map {
            it.asExternalModel()
        }

    override suspend fun addListing(listing: Listing) {
        try {
            dao.addTemplate(listing.toEntity())
            logd("Local DB: listing added")
        } catch (e: Exception) {
            logd("Local DB: Could not add listing")
            throw Exception("Could not add listing")
        }
    }

    override suspend fun deleteAllListings() {
        try {
            dao.deleteAllTemplates()
            logd("Local DB: listings deleted")
        } catch (e: Exception) {
            logd("Local DB: Could not delete listings")
            throw Exception("Could not delete listings")
        }
    }
}