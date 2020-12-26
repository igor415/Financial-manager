package com.varivoda.igor.tvz.financijskimanager.ui.stock

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import com.varivoda.igor.tvz.financijskimanager.model.ProductStockDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StockViewModel (private val storeRepository: BaseStoreRepository): ViewModel(){

    var allProducts = MediatorLiveData<List<ProductStockDTO>>()
    var filter = MutableLiveData<String>()

    var info = MutableLiveData<List<ProductStockDTO>>()

    init {
        allProducts.addSource(filter){ getAllProductsFor() }
        filter.value = ""
    }

    fun getAllProductsFor(){
        viewModelScope.launch(Dispatchers.IO) {
            delay(700)
            if(filter.value == null){
                allProducts.postValue(storeRepository.getAllProductsStockData(""))
            }else{
                allProducts.postValue(storeRepository.getAllProductsStockData(filter.value!!))
            }

        }
    }

    fun getInfo(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            info.postValue(storeRepository.getInfo(id))
        }
    }

    fun getNumberForStoreId(id: Int): String?{
        return storeRepository.getNumberForStoreId(id)
    }
}