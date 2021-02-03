package com.varivoda.igor.tvz.financijskimanager.ui.return_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.BillRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import java.lang.IllegalArgumentException

class ReturnProductViewModelFactory(private val billRepository: BillRepository,
                                    private val productRepository: BaseProductRepository,
                                    private val preferences: Preferences) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ReturnProductViewModel::class.java)){
            return ReturnProductViewModel(billRepository, productRepository, preferences) as T
        }
        throw IllegalArgumentException("wrong argument")
    }


}