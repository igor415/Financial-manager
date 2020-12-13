package com.varivoda.igor.tvz.financijskimanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class InventoryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val storeName: String,
    val date: String,
    val successful: Boolean,
    val fullName: String
)