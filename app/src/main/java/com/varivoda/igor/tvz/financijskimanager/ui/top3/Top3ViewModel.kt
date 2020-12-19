package com.varivoda.igor.tvz.financijskimanager.ui.top3

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import com.varivoda.igor.tvz.financijskimanager.model.CategoryDTO
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentMonth
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentYear
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Top3ViewModel(private val productRepository: BaseProductRepository,
                    private val storeRepository: BaseStoreRepository): ViewModel(){

    var top3Categories = MediatorLiveData<List<CategoryDTO>>()
    var allStores = MutableLiveData<List<Store>>()
    var currentStore = MutableLiveData<Store>()
    var monthAndYear = MutableLiveData<Pair<String, String>>()

    init {
        top3Categories.addSource(currentStore){ getTop3Categories()}
        top3Categories.addSource(monthAndYear){ getTop3Categories()}
        getAllStores()
        monthAndYear.value = Pair(getCurrentMonth(), getCurrentYear())
    }

    fun getTop3Categories(){
        viewModelScope.launch(Dispatchers.IO) {
            if(currentStore.value != null && monthAndYear.value != null){
                top3Categories.postValue(productRepository.getTop3CategoriesAtLeastSold(monthAndYear.value!!.first, monthAndYear.value!!.second, currentStore.value!!.id))
            }else{
                println("debug nooo")
            }

        }
    }

    private fun getAllStores(){
        viewModelScope.launch(Dispatchers.IO) {
            val all = storeRepository.getAllStores().toMutableList()
            all.add(0, Store(-1,"Sve poslovnice",null,1))
            currentStore.postValue(all[0])
            allStores.postValue(all)
        }
    }
}