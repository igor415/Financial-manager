package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Customer
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseCustomerRepository
import com.varivoda.igor.tvz.financijskimanager.model.CategoryDTO
import kotlinx.coroutines.flow.Flow

class CustomerRepository(private val database: AppDatabase) :
    BaseCustomerRepository {

    override fun getAllCustomers(): Flow<List<Customer>>{
        return database.customerDao.getAllCustomers()
    }

    override fun deleteCustomer(id: Int){
        database.customerDao.deleteCustomer(id)
    }

    override fun getCustomersPaging(): Flow<PagingData<Customer>> {
        val factory = {database.customerDao.getCustomersPaging()}
        return Pager(config = PagingConfig(pageSize = 1, enablePlaceholders = false),
        pagingSourceFactory = factory).flow
    }

    override fun getTop3CustomersMostItemsCategory(storeId: Int, month: String, year: String, categoryId: Int): List<CategoryDTO> {
        val allCustomers = database.customerDao.getAllCustomersList()
        var finalList = mutableListOf<CategoryDTO>()
        allCustomers.forEach {
            val num = if(storeId == -1){
                database.customerDao.getTop3CustomersMostItemsCategoryWithoutStore(month, year, categoryId, it.id)
            }else{
                database.customerDao.getTop3CustomersMostItemsCategory(month, year, storeId, categoryId, it.id)
            }
            if(num == null){
                finalList.add(CategoryDTO(0,"${it.customerName} ${it.customerLastName}",0))
            }else{
                finalList.add(CategoryDTO(0,"${it.customerName} ${it.customerLastName}",num.toInt()))
            }
        }


        return finalList.sortedBy { it.count }.reversed().take(3)
    }
}