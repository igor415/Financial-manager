package com.varivoda.igor.tvz.financijskimanager.ui.bills

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.BillRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.EmployeeRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.ProductRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository

class BillViewModelFactory(private val billRepository: BillRepository,
                           private val employeeRepository: EmployeeRepository,
                           private val productRepository: BaseProductRepository
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BillViewModel::class.java)){
            return BillViewModel(billRepository, employeeRepository, productRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}