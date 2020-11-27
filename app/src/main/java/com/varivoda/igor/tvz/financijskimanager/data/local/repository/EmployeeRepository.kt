package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Employee
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseEmployeeRepository
import com.varivoda.igor.tvz.financijskimanager.model.EmployeeDTO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class EmployeeRepository(private val appDatabase: AppDatabase) :
    BaseEmployeeRepository {

    override fun getEmployees(): Flow<List<Employee>> {
        return appDatabase.employeeDao.getEmployees()
    }

    override fun getEmployeesAndStores(): Flow<List<EmployeeDTO>> {
        return appDatabase.employeeDao.getEmployeesAndStores()
    }

    override fun deleteEmployee(id: Int){
        appDatabase.employeeDao.deleteEmployee(id)
    }

    override suspend fun getEmployeeTotalPerMonthAndYear(month: String, year: String): String?{
        return GlobalScope.async {
            appDatabase.employeeDao.getEmployeeMostTotalPerMonthAndYear(month, year)
        }.await()

    }

    override fun getEmployeeMostDaysIssuedInvoice(year: String): String? {
        return appDatabase.employeeDao.getEmployeeMostDaysIssuedInvoice(year)
    }
}