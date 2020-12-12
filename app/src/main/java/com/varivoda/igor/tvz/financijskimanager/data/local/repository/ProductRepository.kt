package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.model.BarChartEntry
import com.varivoda.igor.tvz.financijskimanager.model.ProductQuarterDTO
import com.varivoda.igor.tvz.financijskimanager.model.StatisticsEntry
import com.varivoda.igor.tvz.financijskimanager.util.getMonthWithZero
import kotlinx.coroutines.flow.Flow
import java.util.*

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

    override fun getProducts(): List<Product> {
        return database.productDao.getProducts()
    }

    override fun updateProductImage(image: String, id: Int) {
        database.productDao.updateProductImage(image, id)
    }

    override fun getEntries(dateSelected: String, firstProduct: Product): List<StatisticsEntry>? {
        val splitted = dateSelected.split(".")

        val monthStart = GregorianCalendar(splitted[0].toInt()-1, splitted[0].toInt(), 1)
        val totalDays = monthStart.getActualMaximum(Calendar.DAY_OF_MONTH)
        val list = mutableListOf<StatisticsEntry>()
        for (i in 1 until totalDays+1){
            val num = database.productDao.getStatisticsForProduct(getMonthWithZero(i),splitted[0],splitted[1],firstProduct.id)
            list.add(StatisticsEntry(i.toString(),num))
        }

        return list
    }

    override fun productSmallestShare(month: String, year: String): String? {
        return database.productDao.productSmallestShare(month, year)
    }

    override fun totalPerMonth(month: String?, year: String?): String? {
        return database.productDao.totalPerMonth(month, year)
    }

    override fun getBarChartStatistics(year: String): List<BarChartEntry>? {

        val list = mutableListOf<BarChartEntry>()
        for (i in 1 until 13){
            val num = database.productDao.totalPerMonth(getMonthWithZero(i),year)
            if(num != null){
                list.add(BarChartEntry(getMonthWithZero(i),num))
            }else{
                list.add(BarChartEntry(getMonthWithZero(i),"0"))
            }

        }

        return list
    }


}