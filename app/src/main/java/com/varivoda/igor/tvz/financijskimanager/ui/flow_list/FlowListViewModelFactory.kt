package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FlowListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FlowListViewModel::class.java)){
            return FlowListViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}