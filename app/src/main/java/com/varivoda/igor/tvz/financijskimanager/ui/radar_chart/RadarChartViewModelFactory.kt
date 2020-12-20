package com.varivoda.igor.tvz.financijskimanager.ui.radar_chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import java.lang.IllegalArgumentException

class RadarChartViewModelFactory(private val storeRepository: BaseStoreRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RadarChartViewModel::class.java)){
            return RadarChartViewModel(storeRepository) as T
        }
        throw IllegalArgumentException("wrong argument")
    }

}