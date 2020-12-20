package com.varivoda.igor.tvz.financijskimanager.ui.radar_chart

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import com.varivoda.igor.tvz.financijskimanager.model.AttendanceForStore
import com.varivoda.igor.tvz.financijskimanager.util.CustomPeriod
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentYear
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RadarChartViewModel (private val storeRepository: BaseStoreRepository): ViewModel(){

    var radarChartData = MediatorLiveData<List<AttendanceForStore>>()
    var year = MutableLiveData<String>()
    var enum = MutableLiveData<String>()

    init {
        radarChartData.addSource(year) {getDataForRadarChart()}
        radarChartData.addSource(enum) {getDataForRadarChart()}
        year.value = getCurrentYear()
    }

    fun getDataForRadarChart() {
        if(enum.value != null) {
            viewModelScope.launch(Dispatchers.IO) {
                radarChartData.postValue(storeRepository.getAttendanceForPeriod(getEnumForName(enum.value!!)!!,year.value!!))
            }
        }
    }

    private fun getEnumForName(name: String): CustomPeriod? {
        return CustomPeriod.values().find { it.fullName == name }
    }

}