package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Employee
import com.varivoda.igor.tvz.financijskimanager.model.EmployeeDTO
import kotlinx.coroutines.flow.Flow

class EmployeeRepository(private val appDatabase: AppDatabase){

    fun getEmployees(): Flow<List<Employee>> {
        return appDatabase.employeeDao.getEmployees()
    }

    fun getEmployeesAndStores(): Flow<List<EmployeeDTO>> {
        return appDatabase.employeeDao.getEmployeesAndStores()
    }

    fun deleteEmployee(id: Int){
        appDatabase.employeeDao.deleteEmployee(id)
    }
}