package com.varivoda.igor.tvz.financijskimanager.ui.date_product

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseEmployeeRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import com.varivoda.igor.tvz.financijskimanager.util.getMonthAndYearFormatted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DateAndProductViewModel (private val storeRepository: BaseStoreRepository,
                               private val productRepository: BaseProductRepository,
                                private val employeeRepository: BaseEmployeeRepository): ViewModel(){

    var result = MediatorLiveData<String>()
    var employeeResult = MediatorLiveData<String>()
    var dateSelected = MutableLiveData<String>()
    var productSelected = MutableLiveData<Product>()


    init {
        result.addSource(dateSelected) { getStoreBestSellProduct() }

        result.addSource(productSelected) { getStoreBestSellProduct() }

        employeeResult.addSource(productSelected){ getEmployeeMostProductSell() }

        employeeResult.addSource(dateSelected){ getEmployeeMostProductSell() }

        dateSelected.value = getMonthAndYearFormatted()
    }


    private fun getStoreBestSellProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            val splitted = dateSelected.value!!.split(".")
            if(productSelected.value != null) {
                result.postValue(
                    storeRepository.storeBestSellProduct(
                        splitted[0],
                        splitted[1],
                        productSelected.value!!.id,
                        productSelected.value!!.productName
                    )
                )
            }else{
                result.postValue(null)
            }
        }
    }

    fun getEmployeeMostProductSell(){
        viewModelScope.launch(Dispatchers.IO) {
            if(productSelected.value != null) {
                employeeResult.postValue(
                    employeeRepository.employeeMostProductSell(dateSelected.value!!,productSelected.value!!)
                )
            }else{
                employeeResult.postValue(null)
            }
        }
    }

    var products = MutableLiveData<List<Product>>()
    fun getAllProducts(){
        viewModelScope.launch(Dispatchers.IO) {
            products.postValue(productRepository.getProducts())
        }
    }

    fun insertProduct(product: Product) {
        productSelected.value = product
    }

}