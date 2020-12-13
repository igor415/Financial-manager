package com.varivoda.igor.tvz.financijskimanager.ui.pie_chart

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import com.varivoda.igor.tvz.financijskimanager.model.PieChartEntry
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentYear
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PieChartViewModel(private val storeRepository: BaseStoreRepository) : ViewModel(){


    var pieChartStatistics = MediatorLiveData<List<PieChartEntry>>()
    var yearSelected = MutableLiveData<String>()


    init {
        pieChartStatistics.addSource(yearSelected) { getBarChartStatistics() }

        yearSelected.value = getCurrentYear()
    }


    private fun getBarChartStatistics() {
        viewModelScope.launch(Dispatchers.IO) {
            pieChartStatistics.postValue(storeRepository.storeTotalPerYear(yearSelected.value!!))
        }
    }
}