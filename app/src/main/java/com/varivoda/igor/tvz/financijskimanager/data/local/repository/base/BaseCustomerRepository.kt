package com.varivoda.igor.tvz.financijskimanager.data.local.repository.base

import androidx.paging.PagingData
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Customer
import kotlinx.coroutines.flow.Flow

interface BaseCustomerRepository {
    fun getAllCustomers(): Flow<List<Customer>>
    fun deleteCustomer(id: Int)
    fun getCustomersPaging(): Flow<PagingData<Customer>>?
}