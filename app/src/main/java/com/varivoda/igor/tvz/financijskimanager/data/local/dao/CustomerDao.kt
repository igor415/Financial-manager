package com.varivoda.igor.tvz.financijskimanager.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Customer
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.model.CategoryDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

    @Query("SELECT * FROM Customer")
    fun getAllCustomers(): Flow<List<Customer>>

    @Query("SELECT * FROM Customer")
    fun getAllCustomersList(): List<Customer>

    @Query("""SELECT  SUM(pob.quantity) as count FROM Bill b join ProductsOnBill pob on b.id = pob.billId join Product p on p.id = pob.productId
        where strftime('%m',b.date) = :month and p.categoryId = :categoryId
                and strftime('%Y',b.date) = :year and b.storeId = :storeId and b.customerId = :customerId""")
    fun getTop3CustomersMostItemsCategory(month: String, year: String, storeId: Int, categoryId: Int, customerId: Int): String?

    @Query("""SELECT SUM(pob.quantity) as count FROM Bill b join ProductsOnBill pob on b.id = pob.billId join Product p on p.id = pob.productId
        where strftime('%m',b.date) = :month and p.categoryId = :categoryId
                and strftime('%Y',b.date) = :year and b.customerId = :customerId""")
    fun getTop3CustomersMostItemsCategoryWithoutStore(month: String, year: String, categoryId: Int, customerId: Int): String?

    @Query("DELETE FROM Customer WHERE id = :id")
    fun deleteCustomer(id: Int)

    @Insert
    fun insertCustomer(customer: Customer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCustomers(list: List<Customer>)

    @Query("SELECT * FROM Customer")
    fun getCustomersPaging(): PagingSource<Int,Customer>
}