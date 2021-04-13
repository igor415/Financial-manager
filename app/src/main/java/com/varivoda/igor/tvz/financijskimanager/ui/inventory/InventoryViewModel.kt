package com.varivoda.igor.tvz.financijskimanager.ui.inventory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.InventoryItem
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.StockData
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseInventoryRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import com.varivoda.igor.tvz.financijskimanager.model.InventoryDTO
import com.varivoda.igor.tvz.financijskimanager.model.ProductStockDTO
import com.varivoda.igor.tvz.financijskimanager.util.NetworkResult
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentYear
import com.varivoda.igor.tvz.financijskimanager.util.getInventoryDate
import com.varivoda.igor.tvz.financijskimanager.util.getTodayDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InventoryViewModel(private val inventoryRepository: BaseInventoryRepository,
                         private val storeRepository: BaseStoreRepository,
                         private val productRepository: BaseProductRepository,
                         private val preferences: Preferences) : ViewModel(){

    val inventoryItems = MutableLiveData<List<InventoryItem>>()
    val stores = MutableLiveData<List<Store>>()
    var pickedStore: Store? = null
    var pickedDate: String? = null
    var stockData = MutableLiveData<List<ProductStockDTO>>()
    var inventoryResult = MutableLiveData<NetworkResult<Boolean>>()
    var infoMessage = MutableLiveData<String>()

    init {
        getInventoryItems()
    }

    fun getInventoryItems(){
        viewModelScope.launch(Dispatchers.IO) {
            inventoryItems.postValue(inventoryRepository.getInventoryItems())
        }
    }

    fun executeInventory(list: List<InventoryDTO>){
        viewModelScope.launch(Dispatchers.IO) {
            val res = productRepository.executeInventory(preferences.getUserToken()!!,list)
            when(res){
                is NetworkResult.Success -> {
                    productRepository.addInventoryItem(preferences.getUserToken()!!,
                        InventoryItem(id = 100, fullName = "test", storeName = "Zitnjak",successful = true, date = getInventoryDate()))
                    inventoryRepository.changeStockData(list)
                    infoMessage.postValue("Inventura je izvršena.")
                }
                is NetworkResult.NoNetworkConnection -> infoMessage.postValue("Nema internetske povezivosti")
                is NetworkResult.Error -> infoMessage.postValue("Pojavio se problem. Molimo vas pokušajte kasnije.")
                    else -> infoMessage.postValue("Pojavio se problem. Molimo vas pokušajte kasnije.")
            }
        }
    }

    fun getStockData(){
        viewModelScope.launch(Dispatchers.IO) {
            stockData.postValue(inventoryRepository.getStockData())
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