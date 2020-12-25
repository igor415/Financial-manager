package com.varivoda.igor.tvz.financijskimanager.ui.bar_chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository

class BarChartViewModelFactory(private val productRepository: BaseProductRepository,
                               private val storeRepository: BaseStoreRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BarChartViewModel::class.java)){
            return BarChartViewModel(productRepository, storeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}