package com.varivoda.igor.tvz.financijskimanager.ui.quarter

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class QuarterViewModelFactory(private val context: Context) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(QuarterViewModel::class.java)){
            return QuarterViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}