package com.varivoda.igor.tvz.financijskimanager.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Customer
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

    @Query("SELECT * FROM Customer")
    fun getAllCustomers(): Flow<List<Customer>>

    @Query("DELETE FROM Customer WHERE id = :id")
    fun deleteCustomer(id: Int)

    @Insert
    fun insertCustomer(customer: Customer)

    @Query("SELECT * FROM Customer")
    fun getCustomersPaging(): PagingSource<Int,Customer>
}