package com.varivoda.igor.tvz.financijskimanager.ui.bar_chart

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.model.BarChartEntry
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentYear
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BarChartViewModel(private val productRepository: BaseProductRepository) : ViewModel(){

    var barChartStatistics = MediatorLiveData<List<BarChartEntry>>()
    var yearSelected = MutableLiveData<String>()


    init {
        barChartStatistics.addSource(yearSelected) { getBarChartStatistics() }

        yearSelected.value = getCurrentYear()
    }


    private fun getBarChartStatistics() {
        viewModelScope.launch(Dispatchers.IO) {
            barChartStatistics.postValue(productRepository.getBarChartStatistics(yearSelected.value!!))
        }
    }
}