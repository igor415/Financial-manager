package com.varivoda.igor.tvz.financijskimanager.ui.analysis

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.model.StatisticsEntry
import com.varivoda.igor.tvz.financijskimanager.util.getMonthAndYearFormatted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AnalysisViewModel(private val productRepository: BaseProductRepository): ViewModel(){

    var dateSelected = MutableLiveData<String>()
    var firstProduct = MutableLiveData<Product>()
    var secondProduct = MutableLiveData<Product>()
    var firstProductStatistics: MediatorLiveData<List<StatisticsEntry>> = MediatorLiveData()
    var secondProductStatistics: MediatorLiveData<List<StatisticsEntry>> = MediatorLiveData()

    var currentSelectionFlag = 1

    init {

        firstProductStatistics.addSource(dateSelected) { reloadFirst() }

        firstProductStatistics.addSource(firstProduct) { reloadFirst() }

        secondProductStatistics.addSource(dateSelected) { reloadSecond() }

        secondProductStatistics.addSource(secondProduct) { reloadSecond() }

        dateSelected.value = getMonthAndYearFormatted()
    }


    private fun reloadFirst() {
        viewModelScope.launch(Dispatchers.IO) {
            if(firstProduct.value != null) {
                val entries =
                    productRepository.getEntries(dateSelected.value!!, firstProduct.value!!)
                firstProductStatistics.postValue(entries)
            }else{
                firstProductStatistics.postValue(emptyList())
            }
        }
    }

    private fun reloadSecond() {
        viewModelScope.launch(Dispatchers.IO) {
            if(secondProduct.value != null) {
                val entries =
                    productRepository.getEntries(dateSelected.value!!, secondProduct.value!!)
                secondProductStatistics.postValue(entries)
            }else{
                secondProductStatistics.postValue(emptyList())
            }
        }
    }

    fun setSelectionFlag(i: Int) {
        this.currentSelectionFlag = i
    }

    var products = MutableLiveData<List<Product>>()
    fun getAllProducts(){
        viewModelScope.launch(Dispatchers.IO) {
            products.postValue(productRepository.getProducts())
        }
    }

    fun insertProduct(product: Product) {
        if(currentSelectionFlag == 1){
            firstProduct.value = product
        }else{
            secondProduct.value = product
        }
    }

    fun deleteProduct() {
        if(currentSelectionFlag == 1){
            firstProduct.value = null
        }else{
            secondProduct.value = null
        }
    }
}