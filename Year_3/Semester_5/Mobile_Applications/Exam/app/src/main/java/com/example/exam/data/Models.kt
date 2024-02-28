package com.example.exam.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "listings")
data class ListingEntity(
    @PrimaryKey()
    val id: Int,
    val name: String,
    val date: String,
    val details: String,
    val status: String,
    val viewers: Int,
    val type: String,
    val needsSync: Boolean
)

data class Listing(
    val id: Int,
    val name: String,
    val date: String,
    val details: String,
    val status: String,
    val viewers: Int,
    val type: String,
    var needsSync: Boolean
)

fun Listing.toEntity(): ListingEntity = ListingEntity(
    id = id,
    name = name,
    date = date,
    details = details,
    status = status,
    viewers = viewers,
    type = type,
    needsSync = needsSync
)

fun ListingEntity.asExternalModel(): Listing = Listing(
    id, name, date, details, status, viewers, type, needsSync
)