package com.varivoda.igor.tvz.financijskimanager.ui.invoice

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.BillRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.StoreRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import com.varivoda.igor.tvz.financijskimanager.model.EmployeeBestSale
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentMonth
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentYear
import com.varivoda.igor.tvz.financijskimanager.util.getMonthWithZero
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BestInvoiceViewModel(private val billRepository: BillRepository,
                           private val storeRepository: BaseStoreRepository
) : ViewModel(){

    var result = MediatorLiveData<EmployeeBestSale>()

    var allStores = MutableLiveData<List<Store>>()
    var currentStore = MutableLiveData<Store>()
    var monthAndYear = MutableLiveData<Pair<String, String>>()


    init {
        result.addSource(monthAndYear) { getBestInvoiceEmployee() }
        result.addSource(currentStore) { getBestInvoiceEmployee() }
        getAllStores()
        monthAndYear.value = Pair(getMonthWithZero(getCurrentMonth().toInt()), getCurrentYear())
    }


    private fun getBestInvoiceEmployee() {
        viewModelScope.launch(Dispatchers.IO) {
            if(currentStore.value != null){
                result.postValue(billRepository.getEmployeeBestInvoiceSale(monthAndYear.value!!.first, monthAndYear.value!!.second, currentStore.value!!.id))
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