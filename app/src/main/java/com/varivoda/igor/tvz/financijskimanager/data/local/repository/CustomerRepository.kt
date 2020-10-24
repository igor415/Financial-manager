package com.varivoda.igor.tvz.financijskimanager.data.local.repository

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
}