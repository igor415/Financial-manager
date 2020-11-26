package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Customer
import kotlinx.coroutines.flow.Flow

class CustomerRepository(private val database: AppDatabase){

    fun getAllCustomers(): Flow<List<Customer>>{
        return database.customerDao.getAllCustomers()
    }

    fun deleteCustomer(id: Int){
        database.customerDao.deleteCustomer(id)
    }

    fun getCustomersPaging(): Flow<PagingData<Customer>> {
        val factory = {database.customerDao.getCustomersPaging()}
        return Pager(config = PagingConfig(pageSize = 1, enablePlaceholders = false),
        pagingSourceFactory = factory).flow
    }
}