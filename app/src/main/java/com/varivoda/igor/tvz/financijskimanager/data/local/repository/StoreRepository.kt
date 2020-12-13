package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import com.varivoda.igor.tvz.financijskimanager.model.BarChartEntry
import com.varivoda.igor.tvz.financijskimanager.model.PieChartEntry
import com.varivoda.igor.tvz.financijskimanager.util.getMonthWithZero
import kotlinx.coroutines.flow.Flow
import java.util.*

class StoreRepository (private val database: AppDatabase) :
    BaseStoreRepository {

    override fun getStores(): Flow<List<Store>> {
        return database.storesDao.getStores()
    }

    override fun storeTotalPerYear(year: String): List<PieChartEntry> {
        val list = mutableListOf<PieChartEntry>()
        val allStores = database.storesDao.getAllStores()
        val totalPerYear = database.productDao.totalPerYear(year)
        for (i in allStores.indices){
            val num = database.storesDao.storeTotalPerYear(year, allStores[i].id)
            if(num != null && totalPerYear != null){
                val percent = String.format(Locale.getDefault(), "%.2f", num.toFloat() / totalPerYear.toFloat() * 100)
                list.add(PieChartEntry(allStores[i].storeName!!,percent))
            }else{
                list.add(PieChartEntry(allStores[i].storeName!!,"0"))
            }
        }

        return list
    }

    override fun storeBestSellProduct(month: String, year: String, productId: Int, productName: String): String {
        val result = database.storesDao.storeBestSellProduct(month, year, productId)
        if(result == null){
            return ""
        }
        return "$result $productName unutar izabranog vremenskog razdoblja."
    }
}