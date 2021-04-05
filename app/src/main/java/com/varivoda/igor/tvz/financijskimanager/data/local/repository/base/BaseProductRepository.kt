package com.varivoda.igor.tvz.financijskimanager.data.local.repository.base

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Category
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.InventoryItem
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.model.*
import com.varivoda.igor.tvz.financijskimanager.util.NetworkResult
import kotlinx.coroutines.flow.Flow

interface BaseProductRepository {
    fun getAllProducts() : Flow<List<Product>>
    fun getProductPerQuarter(year: String): List<ProductQuarterDTO>
    fun insertProduct(product: Product)
    fun deleteProduct(id: Int)
    fun getTop10Products(month: String, year: String): LiveData<List<Product>>
    fun getMostItemsOnBill(year: String?): LiveData<String?>
    fun getProductStream(): Flow<PagingData<Product>>?
    fun getProducts(): List<Product>
    fun updateProductImage(image: String, id: Int): Boolean
    fun getEntries(dateSelected: String, firstProduct: Product): List<StatisticsEntry>?
    fun productSmallestShare(month: String, year: String): String?
    fun totalPerMonth(month: String?, year: String?): String?
    fun getBarChartStatistics(year: String, currentStore: Pair<Store, Store>): Pair<List<BarChartEntry>, List<BarChartEntry>>?
    fun getTop3CategoriesAtLeastSold(month: String, year: String,storeId: Int): List<CategoryDTO>
    fun getCategories(): List<Category>
    fun addEditProduct(token: String, product: Product): NetworkResult<Boolean>
    fun returnItems(token: String, list: List<ReturnData>): NetworkResult<Boolean>
    fun executeInventory(token: String, list: List<InventoryDTO>): NetworkResult<Boolean>
    fun addInventoryItem(token: String, inventoryItem: InventoryItem): NetworkResult<Boolean>
    fun checkStockData(): NetworkResult<Boolean>
}