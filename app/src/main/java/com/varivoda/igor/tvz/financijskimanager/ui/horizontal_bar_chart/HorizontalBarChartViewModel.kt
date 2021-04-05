package com.varivoda.igor.tvz.financijskimanager.ui.horizontal_bar_chart

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseEmployeeRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import com.varivoda.igor.tvz.financijskimanager.model.HorizontalBarChartEntry
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentMonth
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentYear
import com.varivoda.igor.tvz.financijskimanager.util.getMonthAndYearFormatted
import com.varivoda.igor.tvz.financijskimanager.util.getMonthWithZero
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HorizontalBarChartViewModel (private val employeeRepository: BaseEmployeeRepository, private val storeRepository: BaseStoreRepository): ViewModel(){

    var barChartStatistics = MediatorLiveData<List<HorizontalBarChartEntry>>()
    //var dateSelected = MutableLiveData<String>()
    var allStores = MutableLiveData<List<Store>>()
    var currentStore = MutableLiveData<Store>()
    var monthAndYear = MutableLiveData<Pair<String, String>>()


    init {
        barChartStatistics.addSource(monthAndYear) { getBarChartStatistics() }
        barChartStatistics.addSource(currentStore) { getBarChartStatistics() }
        getAllStores()
        monthAndYear.value = Pair(getMonthWithZero(getCurrentMonth().toInt()), getCurrentYear())
    }


    private fun getBarChartStatistics() {
        viewModelScope.launch(Dispatchers.IO) {
            if(currentStore.value != null){
                barChartStatistics.postValue(employeeRepository.getHorizontalBarChartData(monthAndYear.value!!.first, monthAndYear.value!!.second, currentStore.value!!.id))
            }
        }
    }

    private fun getAllStores(){
        viewModelScope.launch(Dispatchers.IO) {
            val all = storeRepository.getAllStores().toMutableList()
            all.add(0, Store(-1,"Sve poslovnice",null,"",1))
            currentStore.postValue(all[0])
            allStores.postValue(all)
        }
    }
}