package com.varivoda.igor.tvz.financijskimanager.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ProductsOnBill",
    foreignKeys = [ForeignKey(
        entity = Bill::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("billId"),
        onDelete = CASCADE
    ), ForeignKey(
        entity = Product::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("productId"),
        onDelete = CASCADE
    )],
    indices = [Index("billId"), Index("productId")]
)
data class ProductsOnBill (
    @PrimaryKey(autoGenerate = true)
    var id: Int  = 0,
    var billId: Int  = 0,
    var productId: Int  = 0,
    var quantity: Int = 0
)