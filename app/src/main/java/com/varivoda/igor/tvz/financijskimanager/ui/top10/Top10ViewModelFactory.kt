package com.varivoda.igor.tvz.financijskimanager.ui.top10

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.ProductRepository
import java.lang.IllegalArgumentException

class Top10ViewModelFactory(private val productRepository: ProductRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(Top10ViewModel::class.java)){
            return Top10ViewModel(productRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}