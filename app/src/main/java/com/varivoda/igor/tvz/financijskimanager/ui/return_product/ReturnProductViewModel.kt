package com.varivoda.igor.tvz.financijskimanager.ui.return_product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.BillRepository
import com.varivoda.igor.tvz.financijskimanager.model.EmployeeBestSale
import com.varivoda.igor.tvz.financijskimanager.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ReturnProductViewModel (private val billRepository: BillRepository): ViewModel(){

    var returnResult = MutableLiveData<Boolean>()
    var invoiceInfo = MutableLiveData<EmployeeBestSale>()

    fun returnItem(invoiceNumber: String){
        viewModelScope.launch(Dispatchers.IO) {
            returnResult.postValue(true)
        }
    }

    fun getInvoiceInfo(invoiceNumber: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val x = invoiceNumber.toInt()
                invoiceInfo.postValue(billRepository.getInvoiceInfo(x))
            }catch (ex: Exception){

            }

        }
    }
}