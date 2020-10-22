package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import android.content.Context
import androidx.lifecycle.*
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch

class FlowListViewModel(private val context: Context) : ViewModel(){

    private val productRepository = ProductRepository(AppDatabase.getInstance(context))

    val allProducts: LiveData<List<Product>> = productRepository.getAllProducts().asLiveData()
}