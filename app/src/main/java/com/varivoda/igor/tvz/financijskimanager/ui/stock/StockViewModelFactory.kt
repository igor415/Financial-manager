package com.varivoda.igor.tvz.financijskimanager.ui.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import java.lang.IllegalArgumentException

class StockViewModelFactory (private val storeRepository: BaseStoreRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(StockViewModel::class.java)){
            return StockViewModel(storeRepository) as T
        }
        throw IllegalArgumentException("wrong argument")
    }


}