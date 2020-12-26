package com.varivoda.igor.tvz.financijskimanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StockData(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var idStore: Int,
    var productId: Int,
    var quantity: Int
)