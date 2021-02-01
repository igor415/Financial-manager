package com.varivoda.igor.tvz.financijskimanager.model

import androidx.room.ColumnInfo

data class ProductStockDTO(
    @ColumnInfo(name = "id")
    val productId: Int,
    val productName: String,
    var quantity: Int,
    val idStore: Int,
    var yellow: Boolean = false
)