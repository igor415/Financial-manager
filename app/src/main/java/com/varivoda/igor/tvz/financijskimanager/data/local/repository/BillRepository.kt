package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.model.BillDTO
import com.varivoda.igor.tvz.financijskimanager.model.EmployeeBestSale
import com.varivoda.igor.tvz.financijskimanager.model.PieChartEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class BillRepository(private val database: AppDatabase) {

    private val config = PagedList.Config.Builder()
        .setPageSize(12)
        .setInitialLoadSizeHint(12)
        .setPrefetchDistance(12)
        .setEnablePlaceholders(false).build()

    fun getBills(month: String, year: String): LiveData<PagedList<BillDTO>>{
        get()
        return LivePagedListBuilder(database.billDao.getBills(month, year),config).build()
    }

    fun get(){
            GlobalScope.launch(Dispatchers.IO) {
                println("debug: lista je ${database.billDao.get()}")
            }

    }

    fun getPaymentInfo(month: String, year: String, storeId: Int): List<PieChartEntry>{
        val list = mutableListOf<PieChartEntry>()
        val all = database.billDao.getAllPaymentMethods()
        if(month == "-1"){
            val totalValue = if(storeId == -1){
                database.productDao.totalPerYear(year)
            }else{
                database.productDao.totalPerYearWithStore(year, storeId)
            }
            all.forEach {
                val num = if(storeId == -1){
                    database.billDao.getPaymentMethodTotalWithoutMonthWithoutStore(it.id, year)
                }else{
                    database.billDao.getPaymentMethodTotalWithoutMonth(it.id, year, storeId)
                }
                if(num != null && totalValue != null){
                    val percent = String.format(Locale.getDefault(), "%.2f", num.toFloat() / totalValue.toFloat() * 100)
                    list.add(PieChartEntry(it.name,percent))
                }else{
                    list.add(PieChartEntry(it.name,"0"))
                }
            }
        }else{
            val totalValue = if(storeId == -1){
                database.productDao.totalPerYearAndMonth(month, year)
            }else{
                database.productDao.totalPerYearAndMonthWithStore(month, year, storeId)
            }
            all.forEach {
                val num = if(storeId == -1){
                    database.billDao.getPaymentMethodTotalWithoutStore(it.id, month, year)
                }else{
                    database.billDao.getPaymentMethodTotal(it.id, month, year, storeId)
                }
                if(num != null && totalValue != null){
                    val percent = String.format(Locale.getDefault(), "%.2f", num.toFloat() / totalValue.toFloat() * 100)
                    list.add(PieChartEntry(it.name,percent))
                }else{
                    list.add(PieChartEntry(it.name,"0"))
                }
            }
        }
        return list
    }

    fun getEmployeeBestInvoiceSale(month: String, year: String, storeId: Int): EmployeeBestSale{
        return if(storeId == -1){
            database.billDao.getEmployeeWithBestSaleInvoiceWithoutStore(month, year)
        }else{
            database.billDao.getEmployeeWithBestSaleInvoice(month, year, storeId)
        }

    }

    fun getInvoiceInfo(id: Int): EmployeeBestSale?{
        return database.billDao.getInvoiceInfo(id)
    }
}