package com.varivoda.igor.tvz.financijskimanager.ui.bills

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.BillRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.EmployeeRepository
import com.varivoda.igor.tvz.financijskimanager.model.BillDTO

class BillViewModel(context: Context) : ViewModel(){

    private val billRepository = BillRepository(AppDatabase.getInstance(context))
    private val employeeRepository = EmployeeRepository(AppDatabase.getInstance(context))
    var filteredBills = MutableLiveData<List<BillDTO>>()

    fun getBills(month: String, year: String): LiveData<PagedList<BillDTO>> {
        //delay(3000)
        return billRepository.getBills(month, year)
        /*viewModelScope.launch(Dispatchers.IO) {
            val list = billRepository.getBills(month, year)
            filteredBills.postValue(list)
        }*/
    }

    suspend fun getEmployeeTotalPerMonthAndYear(month: String, year: String): String? {
        return employeeRepository.getEmployeeTotalPerMonthAndYear(month, year)
    }
}