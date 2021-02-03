package com.varivoda.igor.tvz.financijskimanager.ui.return_product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.BillRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.model.DataOnBill
import com.varivoda.igor.tvz.financijskimanager.model.EmployeeBestSale
import com.varivoda.igor.tvz.financijskimanager.model.ReturnData
import com.varivoda.igor.tvz.financijskimanager.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class ReturnProductViewModel (private val billRepository: BillRepository,
                              private val productRepository: BaseProductRepository,
                              private val preferences: Preferences): ViewModel(){

    var returnResult = MutableLiveData<NetworkResult<Boolean>>()
    var invoiceInfo = MutableLiveData<EmployeeBestSale>()

    fun returnItem(list: List<ReturnData>){
        viewModelScope.launch(Dispatchers.IO) {
            val response = productRepository.returnItems(preferences.getUserToken()!!, list)
            returnResult.postValue(response)
        }
    }

    var data = MutableLiveData<List<DataOnBill>>()
    fun getInvoiceProductsAndQuantity(invoiceNumber: String){
        viewModelScope.launch(Dispatchers.IO) {
            data.postValue(billRepository.getDataOnBill(invoiceNumber))
        }
    }

    private var job: Job? = null

    fun getInvoiceInfo(invoiceNumber: String){
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                val x = invoiceNumber.toInt()
                invoiceInfo.postValue(billRepository.getInvoiceInfo(x))
            }catch (ex: Exception){

            }

        }
    }
}