package com.varivoda.igor.tvz.financijskimanager.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.varivoda.igor.tvz.financijskimanager.model.ProductDTO

@Entity(foreignKeys = [ForeignKey(
    entity = Category::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("categoryId"),
    onDelete = ForeignKey.CASCADE
)],
    indices = [Index("categoryId")]
)
data class Product(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var productName: String,
    var price: Double,
    var categoryId: Int,
    var image: String? = null
){
    fun toDTO(): ProductDTO{
        return ProductDTO(productName,price)
    }
}