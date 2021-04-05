package com.varivoda.igor.tvz.financijskimanager.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel(private val productRepository: BaseProductRepository) : ViewModel(){

    var stockDataNotification = MutableLiveData<NetworkResult<Boolean>>()

    init {
        checkStockData()
    }

    private fun checkStockData(){
        viewModelScope.launch(Dispatchers.IO) {
            val response = productRepository.checkStockData()
            delay(3000)
            stockDataNotification.postValue(response)
        }
    }
}