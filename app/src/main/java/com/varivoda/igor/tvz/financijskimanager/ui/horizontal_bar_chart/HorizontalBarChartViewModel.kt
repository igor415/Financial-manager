package com.varivoda.igor.tvz.financijskimanager.ui.horizontal_bar_chart

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseEmployeeRepository
import com.varivoda.igor.tvz.financijskimanager.model.HorizontalBarChartEntry
import com.varivoda.igor.tvz.financijskimanager.util.getMonthAndYearFormatted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HorizontalBarChartViewModel (private val employeeRepository: BaseEmployeeRepository): ViewModel(){

    var barChartStatistics = MediatorLiveData<List<HorizontalBarChartEntry>>()
    var dateSelected = MutableLiveData<String>()


    init {
        barChartStatistics.addSource(dateSelected) { getBarChartStatistics() }

        dateSelected.value = getMonthAndYearFormatted()
    }


    private fun getBarChartStatistics() {
        viewModelScope.launch(Dispatchers.IO) {
            barChartStatistics.postValue(employeeRepository.getHorizontalBarChartData(dateSelected.value!!))
        }
    }
}