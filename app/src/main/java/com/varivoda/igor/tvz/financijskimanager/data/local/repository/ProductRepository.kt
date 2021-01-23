package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Category
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.data.local.remote.Api
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.model.BarChartEntry
import com.varivoda.igor.tvz.financijskimanager.model.CategoryDTO
import com.varivoda.igor.tvz.financijskimanager.model.ProductQuarterDTO
import com.varivoda.igor.tvz.financijskimanager.model.StatisticsEntry
import com.varivoda.igor.tvz.financijskimanager.monitoring.ConnectivityAgent
import com.varivoda.igor.tvz.financijskimanager.util.getMonthWithZero
import kotlinx.coroutines.flow.Flow
import java.util.*

class ProductRepository(private val database: AppDatabase,
                        private val preferences: Preferences, private val connectivityAgent: ConnectivityAgent) :
    BaseProductRepository {

    val api = Api.retrofitService

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
        return if(month == "-1"){
            database.productDao.top10ProductsOnlyYear(year)
        }else{
            database.productDao.top10Products(month, year)
        }

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

    override fun updateProductImage(image: String, id: Int): Boolean {
        val product = database.productDao.getProductById(id)
        if (product != null){
            return if(connectivityAgent.isDeviceConnectedToInternet){
                val response = api.sendImageString(preferences.getUserToken()!!, product).execute()
                if(response.isSuccessful){
                    database.productDao.updateProductImage(image, id)
                    true
                }else{
                    false
                }
            }else{
                false
            }

        }else{
            return false
        }


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

    override fun getBarChartStatistics(year: String, currentStore: Pair<Store, Store>): Pair<List<BarChartEntry>, List<BarChartEntry>>? {

        val list1 = mutableListOf<BarChartEntry>()
        val list2 = mutableListOf<BarChartEntry>()
            var num = 0.0
            for(i in 1 until 4){
                num += database.productDao.totalPerMonthForBarChart(getMonthWithZero(i),year,currentStore.first.id) ?: 0.0
            }
        list1.add(BarChartEntry("1",num))

        num = 0.0
        for(i in 4 until 7){
            num += database.productDao.totalPerMonthForBarChart(getMonthWithZero(i),year,currentStore.first.id) ?: 0.0
        }
        list1.add(BarChartEntry("2",num))

        num = 0.0
        for(i in 7 until 10){
            num += database.productDao.totalPerMonthForBarChart(getMonthWithZero(i),year,currentStore.first.id) ?: 0.0
        }
        list1.add(BarChartEntry("3",num))
        num = 0.0
        for(i in 10 until 13){
            num += database.productDao.totalPerMonthForBarChart(getMonthWithZero(i),year,currentStore.first.id) ?: 0.0
        }
        list1.add(BarChartEntry("4",num))


        var num1 = 0.0
        for(i in 1 until 4){
            num1 += database.productDao.totalPerMonthForBarChart(getMonthWithZero(i),year,currentStore.second.id) ?: 0.0
        }
        list2.add(BarChartEntry("1",num1))

        num1 = 0.0
        for(i in 4 until 7){
            num1 += database.productDao.totalPerMonthForBarChart(getMonthWithZero(i),year,currentStore.second.id) ?: 0.0
        }
        list2.add(BarChartEntry("2",num1))

        num1 = 0.0
        for(i in 7 until 10){
            num1 += database.productDao.totalPerMonthForBarChart(getMonthWithZero(i),year,currentStore.second.id) ?: 0.0
        }
        list2.add(BarChartEntry("3",num1))

        num1 = 0.0
        for(i in 10 until 13){
            num1 += database.productDao.totalPerMonthForBarChart(getMonthWithZero(i),year,currentStore.second.id) ?: 0.0
        }
        list2.add(BarChartEntry("4",num1))




        return Pair(list1, list2)
    }

    override fun getTop3CategoriesAtLeastSold(month: String, year: String,storeId: Int): List<CategoryDTO> {
        val allCategories = database.categoryDao.getAllCategories()
        val categoriesDTO = mutableListOf<CategoryDTO>()
        allCategories.forEach {
            val count = if(storeId == -1){
                if(month == "-1"){
                    database.categoryDao.getTop3CategoriesAtLeastSoldInOnlyYear(year, it.id)
                }else{
                    database.categoryDao.getTop3CategoriesAtLeastSoldInYearAndMonth(month, year, it.id)
                }

            }else{
                if(month == "-1") {
                    database.categoryDao.getTop3CategoriesAtLeastSoldInOnlyYearWithStore(year, it.id, storeId)
                }else{
                    database.categoryDao.getTop3CategoriesAtLeastSoldInYearAndMonthWithStore(month, year, it.id, storeId)
                }

            }

            categoriesDTO.add(CategoryDTO(it.id,it.name,count))
        }
        return categoriesDTO.sortedBy { it.count }.take(3)
    }

    override fun getCategories(): List<Category> {
        return database.categoryDao.getAllCategories()
    }


}