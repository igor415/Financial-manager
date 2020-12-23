package com.varivoda.igor.tvz.financijskimanager.ui.time_of_day

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import java.lang.IllegalArgumentException

class TimeOfDayViewModelFactory (private val storeRepository: BaseStoreRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TimeOfDayViewModel::class.java)){
            return TimeOfDayViewModel(storeRepository) as T
        }
        throw IllegalArgumentException("wrong argument")
    }


}