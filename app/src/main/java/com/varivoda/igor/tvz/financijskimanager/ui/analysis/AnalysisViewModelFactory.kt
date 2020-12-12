package com.varivoda.igor.tvz.financijskimanager.ui.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository

class AnalysisViewModelFactory(private val productRepository: BaseProductRepository): ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AnalysisViewModel::class.java)){
            return AnalysisViewModel(productRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}