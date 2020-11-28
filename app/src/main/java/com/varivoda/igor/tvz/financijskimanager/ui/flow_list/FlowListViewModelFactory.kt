package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.CustomerRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.EmployeeRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.ProductRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.StoreRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository

class FlowListViewModelFactory(
    private val storeRepository: StoreRepository,
    private val productRepository: BaseProductRepository,
    private val employeeRepository: EmployeeRepository,
    private val customerRepository: CustomerRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FlowListViewModel::class.java)){
            return FlowListViewModel(storeRepository,productRepository, employeeRepository, customerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}