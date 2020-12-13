package com.varivoda.igor.tvz.financijskimanager.ui.pie_chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository

class PieChartViewModelFactory(private val storeRepository: BaseStoreRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PieChartViewModel::class.java)){
            return PieChartViewModel(storeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}