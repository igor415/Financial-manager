package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import android.content.Context
import androidx.lifecycle.*
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Customer
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Employee
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.CustomerRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.EmployeeRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.ProductRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.StoreRepository
import com.varivoda.igor.tvz.financijskimanager.model.EmployeeDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class FlowListViewModel(context: Context) : ViewModel(){

    private val productRepository = ProductRepository(AppDatabase.getInstance(context))
    private val employeeRepository = EmployeeRepository(AppDatabase.getInstance(context))
    private val customerRepository = CustomerRepository(AppDatabase.getInstance(context))
    private val storeRepository = StoreRepository(AppDatabase.getInstance(context))

    val allProducts: Flow<List<Product>> = productRepository.getAllProducts()
        .onEach { it.map { product -> product.productName = "${product.productName} : ${product.price} kn" } } .flowOn(Dispatchers.Main).conflate()

    val employees: Flow<List<EmployeeDTO>> = employeeRepository.getEmployeesAndStores()
        .onEach { it.map { employee -> employee.employeeName = "${employee.employeeName} ${employee.employeeLastName} (${employee.storeName})" } }
        .flowOn(Dispatchers.Default).conflate()

    val customers: Flow<List<Customer>> = customerRepository.getAllCustomers()
        .flowOn(Dispatchers.Default).conflate()

    val stores: Flow<List<Store>> = storeRepository.getStores()
        .flowOn(Dispatchers.Default).conflate()

    fun deleteCustomer(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            customerRepository.deleteCustomer(id)
        }

    }

    fun deleteEmployee(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            employeeRepository.deleteEmployee(id)
        }

    }

}