package com.varivoda.igor.tvz.financijskimanager.ui.horizontal_bar_chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseEmployeeRepository
import java.lang.IllegalArgumentException

class HorizontalBarChartViewModelFactory(private val employeeRepository: BaseEmployeeRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HorizontalBarChartViewModel::class.java)){
            return HorizontalBarChartViewModel(employeeRepository) as T
        }
        throw IllegalArgumentException("wrong argument")
    }

}