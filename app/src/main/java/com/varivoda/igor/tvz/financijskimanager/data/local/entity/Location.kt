package com.varivoda.igor.tvz.financijskimanager.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Location",
    foreignKeys = [ForeignKey(
        entity = County::class,
        parentColumns = arrayOf("countyId"),
        childColumns = arrayOf("countyId"),
        onDelete = CASCADE
    )],
    indices = [Index("countyId")]
)
data class Location (
    @PrimaryKey
    var locationId: Int = 0,
    var locationName: String? = null,
    var countyId: Int = 0
)