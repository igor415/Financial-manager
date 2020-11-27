package com.varivoda.igor.tvz.financijskimanager.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.PagingData
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.model.ProductQuarterDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeProductRepository :
    BaseProductRepository {
    override fun getAllProducts(): Flow<List<Product>> {
        return flow { listOf(Product(1,"name",12.0,1)) }
    }

    override fun getProductPerQuarter(year: String): List<ProductQuarterDTO> {
        return listOf(ProductQuarterDTO("first","name","21"))
    }

    override fun insertProduct(product: Product) {

    }

    override fun deleteProduct(id: Int) {

    }

    override fun getTop10Products(month: String, year: String): LiveData<List<Product>> {
        return liveData { listOf(Product(1,"name",12.0,1)) }
    }

    override fun getMostItemsOnBill(year: String?): LiveData<String?> {
        return liveData { "string" }
    }

    override fun getProductStream(): Flow<PagingData<Product>>? {
        return null
    }

}