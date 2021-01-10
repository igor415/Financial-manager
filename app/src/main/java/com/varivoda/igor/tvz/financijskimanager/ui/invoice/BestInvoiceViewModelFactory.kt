package com.varivoda.igor.tvz.financijskimanager.ui.invoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.BillRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.StoreRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import java.lang.IllegalArgumentException

class BestInvoiceViewModelFactory (private val billRepository: BillRepository, private val storeRepository: BaseStoreRepository): ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BestInvoiceViewModel::class.java)){
            return BestInvoiceViewModel(billRepository, storeRepository) as T
        }
        throw IllegalArgumentException("wrong argument")
    }

}