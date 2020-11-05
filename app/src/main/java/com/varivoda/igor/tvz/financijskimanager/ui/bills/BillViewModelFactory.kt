package com.varivoda.igor.tvz.financijskimanager.ui.bills

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BillViewModelFactory(private val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BillViewModel::class.java)){
            return BillViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}