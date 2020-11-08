package com.varivoda.igor.tvz.financijskimanager.ui.quarter

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.ProductRepository
import com.varivoda.igor.tvz.financijskimanager.model.ProductQuarterDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuarterViewModel (private val context: Context): ViewModel(){

    private val productRepository = ProductRepository(AppDatabase.getInstance(context))

    var productsPerQuarter = MutableLiveData<List<ProductQuarterDTO>>()

    fun getProductPerQuarter(year: String){
        viewModelScope.launch(Dispatchers.IO) {
            productsPerQuarter.postValue(productRepository.getProductPerQuarter(year))
        }
    }
}