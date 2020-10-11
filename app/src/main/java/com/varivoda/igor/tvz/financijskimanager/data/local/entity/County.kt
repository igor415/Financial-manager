package com.varivoda.igor.tvz.financijskimanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "County")
data class County(
    @PrimaryKey(autoGenerate = true)
    var countyId: Int = 0,
    var countyName: String? = null
)