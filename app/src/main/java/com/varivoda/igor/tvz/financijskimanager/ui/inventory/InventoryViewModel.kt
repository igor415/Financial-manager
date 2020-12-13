package com.varivoda.igor.tvz.financijskimanager.ui.inventory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.InventoryItem
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseInventoryRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import com.varivoda.igor.tvz.financijskimanager.util.getTodayDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InventoryViewModel(private val inventoryRepository: BaseInventoryRepository,
                         private val storeRepository: BaseStoreRepository) : ViewModel(){

    val inventoryItems = MutableLiveData<List<InventoryItem>>()
    val stores = MutableLiveData<List<Store>>()
    var pickedStore: Store? = null
    var pickedDate: String? = null

    init {
        getInventoryItems()
    }

    private fun getInventoryItems(){
        viewModelScope.launch(Dispatchers.IO) {
            inventoryItems.postValue(inventoryRepository.getInventoryItems())
        }
    }

    fun getStores(){
        viewModelScope.launch(Dispatchers.IO) {
            stores.postValue(storeRepository.getAllStores())
        }
    }

    fun checkIfInfoInserted(): Boolean{
        return pickedDate != null && pickedStore != null
    }
}