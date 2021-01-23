package com.varivoda.igor.tvz.financijskimanager.ui.pie_chart

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.BillRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import com.varivoda.igor.tvz.financijskimanager.model.PieChartEntry
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentMonth
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentYear
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PieChartViewModel(private val storeRepository: BaseStoreRepository, private val billRepository: BillRepository) : ViewModel(){


    var pieChartStatistics = MediatorLiveData<List<PieChartEntry>>()
    var monthAndYear = MutableLiveData<Pair<String,String>>()
    var allStores = MutableLiveData<List<Store>>()
    var currentStore = MutableLiveData<Store>()
    var paymentInfo = MediatorLiveData<List<PieChartEntry>>()

    init {
        pieChartStatistics.addSource(monthAndYear) { getBarChartStatistics() }

        paymentInfo.addSource(monthAndYear){ getPaymentInfo() }
        paymentInfo.addSource(currentStore){ getPaymentInfo() }
        getAllStores()
        monthAndYear.value = Pair(getCurrentMonth(), getCurrentYear())
    }


    private fun getBarChartStatistics() {
        viewModelScope.launch(Dispatchers.IO) {
            pieChartStatistics.postValue(storeRepository.storeTotalPerYear(monthAndYear.value!!.first, monthAndYear.value!!.second))
        }
    }

    private fun getPaymentInfo(){
        viewModelScope.launch(Dispatchers.IO) {
            if(currentStore.value != null){
                val list = billRepository.getPaymentInfo(monthAndYear.value!!.first, monthAndYear.value!!.second, currentStore.value!!.id)
                list.forEach {
                    it.total = it.total.replace(",",".")
                }
                paymentInfo.postValue(list)
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