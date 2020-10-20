package com.varivoda.igor.tvz.financijskimanager.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Customer",
    foreignKeys = [ForeignKey(
        entity = Location::class,
        parentColumns = arrayOf("locationId"),
        childColumns = arrayOf("locationId"),
        onDelete = CASCADE
    )],
    indices = [Index("locationId")]
)
data class Customer (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var customerName: String? = null,
    var customerLastName: String? = null,
    private val address: String? = null,
    var points: Int = 0,
    var locationId: Int = 0
)