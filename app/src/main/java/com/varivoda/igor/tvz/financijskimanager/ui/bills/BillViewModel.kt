package com.varivoda.igor.tvz.financijskimanager.ui.bills

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.BillRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BillViewModel(context: Context) : ViewModel(){

    private val billRepository = BillRepository(AppDatabase.getInstance(context))

    fun getBills(month: String, year: String){
        viewModelScope.launch(Dispatchers.IO) {
            val list = billRepository.getBills(month, year)
            println("debug list is $list")
        }
    }
}