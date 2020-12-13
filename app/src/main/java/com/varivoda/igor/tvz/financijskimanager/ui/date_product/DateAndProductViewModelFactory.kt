package com.varivoda.igor.tvz.financijskimanager.ui.date_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import java.lang.IllegalArgumentException

class DateAndProductViewModelFactory(private val storeRepository: BaseStoreRepository,
                                     private val productRepository: BaseProductRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DateAndProductViewModel::class.java)){
            return DateAndProductViewModel(storeRepository,productRepository) as T
        }
        throw IllegalArgumentException("wrong argument")
    }

}