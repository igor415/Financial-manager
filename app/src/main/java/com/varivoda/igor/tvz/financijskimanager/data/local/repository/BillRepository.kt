package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Bill
import com.varivoda.igor.tvz.financijskimanager.model.BarChartEntry
import com.varivoda.igor.tvz.financijskimanager.model.BillDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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


}