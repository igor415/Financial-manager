package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import android.content.Context
import androidx.lifecycle.*
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Employee
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.EmployeeRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.ProductRepository
import com.varivoda.igor.tvz.financijskimanager.model.EmployeeDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FlowListViewModel(private val context: Context) : ViewModel(){

    private val productRepository = ProductRepository(AppDatabase.getInstance(context))
    private val employeeRepository = EmployeeRepository(AppDatabase.getInstance(context))

    val allProducts: Flow<List<Product>> = productRepository.getAllProducts()
        .onEach { it.map { product -> product.productName = "${product.productName} : ${product.price} kn" } } .flowOn(Dispatchers.Main).conflate()

    val employees: Flow<List<EmployeeDTO>> = employeeRepository.getEmployeesAndStores()
        .onEach { it.map { employee -> employee.employeeName = "${employee.employeeName} ${employee.employeeLastName} (${employee.storeName})" } }
        .flowOn(Dispatchers.Default).conflate()

}