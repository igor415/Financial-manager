package com.varivoda.igor.tvz.financijskimanager.repository

import androidx.paging.PagingData
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Customer
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseCustomerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCustomerRepository :
    BaseCustomerRepository {
    override fun getAllCustomers(): Flow<List<Customer>> {
        return flow { listOf(Customer(1,"franjo","papa",null,0,0),
            Customer(1,"zeljko","manojlovic",null,0,0)) }
    }

    override fun deleteCustomer(id: Int) {

    }

    override fun getCustomersPaging(): Flow<PagingData<Customer>>? {
        return null
    }

}