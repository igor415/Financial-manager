package com.varivoda.igor.tvz.financijskimanager.ui.return_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.BillRepository
import java.lang.IllegalArgumentException

class ReturnProductViewModelFactory(private val billRepository: BillRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ReturnProductViewModel::class.java)){
            return ReturnProductViewModel(billRepository) as T
        }
        throw IllegalArgumentException("wrong argument")
    }


}