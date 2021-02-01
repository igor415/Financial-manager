package com.varivoda.igor.tvz.financijskimanager.ui.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseInventoryRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import java.lang.IllegalArgumentException

class InventoryViewModelFactory(private val inventoryRepository: BaseInventoryRepository,
                                private val storeRepository: BaseStoreRepository,
                                private val productRepository: BaseProductRepository,
                                private val preferences: Preferences) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(InventoryViewModel::class.java)){
            return InventoryViewModel(inventoryRepository,storeRepository, productRepository, preferences) as T
        }
        throw IllegalArgumentException("wrong argument")
    }

}