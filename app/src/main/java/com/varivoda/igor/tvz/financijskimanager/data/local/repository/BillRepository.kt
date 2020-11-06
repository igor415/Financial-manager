package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Bill
import com.varivoda.igor.tvz.financijskimanager.model.BillDTO

class BillRepository(private val database: AppDatabase) {

    private val config = PagedList.Config.Builder()
        .setPageSize(3)
        .setInitialLoadSizeHint(3)
        .setPrefetchDistance(3)
        .setEnablePlaceholders(false).build()

    fun getBills(month: String, year: String): LiveData<PagedList<BillDTO>>{
        return LivePagedListBuilder(database.billDao.getBills(month, year),config).build()
    }

   /* fun get(){
        println("debug: lista je ${database.billDao.get()}")
    }*/
}