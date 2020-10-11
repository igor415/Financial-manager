package com.varivoda.igor.tvz.financijskimanager.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Store",
    foreignKeys = [ForeignKey(
        entity = Location::class,
        parentColumns = arrayOf("locationId"),
        childColumns = arrayOf("locationId"),
        onDelete = CASCADE
    )],
    indices = [Index("locationId")]
)
data class Store (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var storeName: String? = null,
    var address: String? = null,
    var locationId: Int = 0
)