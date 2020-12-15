package com.varivoda.igor.tvz.financijskimanager.ui.date_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseEmployeeRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import java.lang.IllegalArgumentException

class DateAndProductViewModelFactory(private val storeRepository: BaseStoreRepository,
                                     private val productRepository: BaseProductRepository,
                                    private val employeeRepository: BaseEmployeeRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DateAndProductViewModel::class.java)){
            return DateAndProductViewModel(storeRepository,productRepository,employeeRepository) as T
        }
        throw IllegalArgumentException("wrong argument")
    }

}