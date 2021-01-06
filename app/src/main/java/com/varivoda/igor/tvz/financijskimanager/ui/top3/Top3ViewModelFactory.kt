package com.varivoda.igor.tvz.financijskimanager.ui.top3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseCustomerRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import java.lang.IllegalArgumentException

class Top3ViewModelFactory(private val productRepository: BaseProductRepository, private val storeRepository: BaseStoreRepository,
                           private val customerRepository: BaseCustomerRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(Top3ViewModel::class.java)){
            return Top3ViewModel(productRepository, storeRepository, customerRepository) as T
        }
        throw IllegalArgumentException("wrong argument")
    }


}