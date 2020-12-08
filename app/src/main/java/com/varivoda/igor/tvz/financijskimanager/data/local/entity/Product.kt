package com.varivoda.igor.tvz.financijskimanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.varivoda.igor.tvz.financijskimanager.model.ProductDTO

@Entity
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