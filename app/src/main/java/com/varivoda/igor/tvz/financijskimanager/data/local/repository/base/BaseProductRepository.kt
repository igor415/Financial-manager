package com.varivoda.igor.tvz.financijskimanager.data.local.repository.base

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.model.ProductQuarterDTO
import kotlinx.coroutines.flow.Flow

interface BaseProductRepository {
    fun getAllProducts() : Flow<List<Product>>
    fun getProductPerQuarter(year: String): List<ProductQuarterDTO>
    fun insertProduct(product: Product)
    fun deleteProduct(id: Int)
    fun getTop10Products(month: String, year: String): LiveData<List<Product>>
    fun getMostItemsOnBill(year: String?): LiveData<String?>
    fun getProductStream(): Flow<PagingData<Product>>?
}