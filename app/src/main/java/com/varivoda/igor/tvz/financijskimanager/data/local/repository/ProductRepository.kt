package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import androidx.lifecycle.LiveData
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.model.ProductQuarterDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow

class ProductRepository(private val database: AppDatabase){

    fun getAllProducts() : Flow<List<Product>>{
        return database.productDao.getAllProducts()
    }

    fun getProductPerQuarter(year: String): List<ProductQuarterDTO> {
        return database.productDao.productPerQuarter(year)
    }

    fun insertProduct(product: Product){
        database.productDao.insertProduct(product)
    }

    fun deleteProduct(id: Int) {
        database.productDao.deleteProduct(id)
    }

    fun getTop10Products(month: String, year: String): LiveData<List<Product>>{
        return database.productDao.top10Products(month, year)
    }
}