package com.varivoda.igor.tvz.financijskimanager.ui.top3

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Category
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseCustomerRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import com.varivoda.igor.tvz.financijskimanager.model.CategoryDTO
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentMonth
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentYear
import com.varivoda.igor.tvz.financijskimanager.util.getMonthWithZero
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Top3ViewModel(private val productRepository: BaseProductRepository,
                    private val storeRepository: BaseStoreRepository,
                    private val customerRepository: BaseCustomerRepository): ViewModel(){

    var top3Categories = MediatorLiveData<List<CategoryDTO>>()
    var top3Customers = MediatorLiveData<List<CategoryDTO>>()
    var allStores = MutableLiveData<List<Store>>()
    var currentStore = MutableLiveData<Store>()
    var allCategories = MutableLiveData<List<Category>>()
    var currentCategory = MutableLiveData<Category>()
    var monthAndYear = MutableLiveData<Pair<String, String>>()

    init {
        top3Categories.addSource(currentStore){ getTop3Categories()}
        top3Categories.addSource(monthAndYear){ getTop3Categories()}

        top3Customers.addSource(currentStore){ getTop3Customers() }
        top3Customers.addSource(monthAndYear){ getTop3Customers() }
        top3Customers.addSource(currentCategory){ getTop3Customers() }

        getAllStores()
        getAllCategories()
        monthAndYear.value = Pair(getMonthWithZero(getCurrentMonth().toInt()), getCurrentYear())
    }

    fun getTop3Categories(){
        viewModelScope.launch(Dispatchers.IO) {
            if(currentStore.value != null && monthAndYear.value != null){
                top3Categories.postValue(productRepository.getTop3CategoriesAtLeastSold(monthAndYear.value!!.first, monthAndYear.value!!.second, currentStore.value!!.id))
            }

        }
    }

    fun getTop3Customers(){
        viewModelScope.launch(Dispatchers.IO) {
            if(currentStore.value != null && monthAndYear.value != null && currentCategory.value != null){
                top3Customers.postValue(
                    customerRepository.getTop3CustomersMostItemsCategory(currentStore.value!!.id,monthAndYear.value!!.first,
                        monthAndYear.value!!.second,currentCategory.value!!.id))
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

    private fun getAllCategories(){
        viewModelScope.launch(Dispatchers.IO) {
            val all = productRepository.getCategories()
            if(all.isNotEmpty()){
                allCategories.postValue(all)
                currentCategory.postValue(all[0])
            }
        }
    }
}