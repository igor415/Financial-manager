package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Bill
import com.varivoda.igor.tvz.financijskimanager.model.BillDTO

class BillRepository(private val database: AppDatabase) {

    fun getBills(month: String, year: String): List<BillDTO>{
        return database.billDao.getBills(month, year)
    }

    fun get(){
        println("debug: lista je ${database.billDao.get()}")
    }
}