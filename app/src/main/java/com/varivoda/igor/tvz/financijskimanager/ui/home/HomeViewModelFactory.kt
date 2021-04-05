package com.varivoda.igor.tvz.financijskimanager.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import java.lang.IllegalArgumentException

class HomeViewModelFactory(private val productRepository: BaseProductRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(productRepository) as T
        }
        throw IllegalArgumentException("wrong argument")
    }

}