package com.varivoda.igor.tvz.financijskimanager.ui.bar_chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository

class BarChartViewModelFactory(private val productRepository: BaseProductRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BarChartViewModel::class.java)){
            return BarChartViewModel(productRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}