package com.varivoda.igor.tvz.financijskimanager.ui.bar_chart

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import com.varivoda.igor.tvz.financijskimanager.model.BarChartEntry
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentYear
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BarChartViewModel(private val productRepository: BaseProductRepository, private val storeRepository: BaseStoreRepository) : ViewModel(){

    var barChartStatistics = MediatorLiveData<Pair<List<BarChartEntry>, List<BarChartEntry>>>()
    var yearSelected = MutableLiveData<String>()
    var allStores = MutableLiveData<List<Store>>()
    var currentStore = MutableLiveData<Pair<Store, Store>>()


    init {
        barChartStatistics.addSource(yearSelected) { getBarChartStatistics() }
        barChartStatistics.addSource(currentStore) { getBarChartStatistics() }
        getAllStores()
        yearSelected.value = getCurrentYear()
    }


    private fun getBarChartStatistics() {
        viewModelScope.launch(Dispatchers.IO) {
            if(currentStore.value != null) {
                barChartStatistics.postValue(productRepository.getBarChartStatistics(yearSelected.value!!, currentStore.value!!))
            }
        }
    }

    private fun getAllStores(){
        viewModelScope.launch(Dispatchers.IO) {
            val all = storeRepository.getAllStores().toMutableList()
            allStores.postValue(all)
        }
    }
}