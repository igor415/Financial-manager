package com.varivoda.igor.tvz.financijskimanager.ui.quarter

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.ProductRepository
import java.lang.IllegalArgumentException

class QuarterViewModelFactory(private val productRepository: ProductRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(QuarterViewModel::class.java)){
            return QuarterViewModel(productRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}