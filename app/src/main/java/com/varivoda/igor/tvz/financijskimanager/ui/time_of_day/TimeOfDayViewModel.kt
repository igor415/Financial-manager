package com.varivoda.igor.tvz.financijskimanager.ui.time_of_day

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import com.varivoda.igor.tvz.financijskimanager.model.TimeOfDayData
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentMonth
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentYear
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TimeOfDayViewModel (private val storeRepository: BaseStoreRepository) : ViewModel(){

    var timeOfDayData = MediatorLiveData<List<TimeOfDayData>>()
    var allStores = MutableLiveData<List<Store>>()
    var currentStore = MutableLiveData<Store>()
    var monthAndYear = MutableLiveData<Pair<String, String>>()

    init {
        timeOfDayData.addSource(currentStore){ getTimeOfDayData()}
        timeOfDayData.addSource(monthAndYear){ getTimeOfDayData()}
        getAllStores()
        monthAndYear.value = Pair(getCurrentMonth(), getCurrentYear())
    }

    fun getTimeOfDayData(){
        viewModelScope.launch(Dispatchers.IO) {
            if(currentStore.value != null && monthAndYear.value != null){
                timeOfDayData.postValue(storeRepository.getChartDataForTimeOfDay(monthAndYear.value!!.first, monthAndYear.value!!.second, currentStore.value!!))
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