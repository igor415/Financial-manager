package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.model.ProductQuarterDTO
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val database: AppDatabase) :
    BaseProductRepository {

    override fun getAllProducts() : Flow<List<Product>>{
        return database.productDao.getAllProducts()
    }

    override fun getProductPerQuarter(year: String): List<ProductQuarterDTO> {
        return database.productDao.productPerQuarter(year)
    }

    override fun insertProduct(product: Product){
        database.productDao.insertProduct(product)
    }

    override fun deleteProduct(id: Int) {
        database.productDao.deleteProduct(id)
    }

    override fun getTop10Products(month: String, year: String): LiveData<List<Product>>{
        return database.productDao.top10Products(month, year)
    }

    override fun getMostItemsOnBill(year: String?): LiveData<String?> {
        return database.productDao.mostItemsOnBill(year)
    }

    override fun getProductStream(): Flow<PagingData<Product>>{
        val pagingSourceFactory = {database.productDao.getAllProductsPaging()}

        return Pager(config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        pagingSourceFactory = pagingSourceFactory).flow
    }
}