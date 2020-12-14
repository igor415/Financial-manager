package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Employee
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseEmployeeRepository
import com.varivoda.igor.tvz.financijskimanager.model.EmployeeDTO
import com.varivoda.igor.tvz.financijskimanager.model.HorizontalBarChartEntry
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

    override fun insertEmployee(employee: Employee) {
        appDatabase.employeeDao.insertEmployee(employee)
    }

    override fun getHorizontalBarChartData(dateSelected: String): List<HorizontalBarChartEntry> {
        val splitted = dateSelected.split(".")
        val allEmployees = appDatabase.employeeDao.getAll()
        val list = mutableListOf<HorizontalBarChartEntry>()
        allEmployees.forEach {
            val x = appDatabase.employeeDao.getEmployeeTotalInvoicesPerMonth(splitted[0],splitted[1],it.id)
            if(x != null){
                list.add(HorizontalBarChartEntry("${it.employeeName.first()}. ${it.employeeLastName}", x.toFloat()))
            }else{
                list.add(HorizontalBarChartEntry("${it.employeeName.first()}. ${it.employeeLastName}", 0f))
            }
        }
        list.sortBy { it.totalInvoice }
        return list
    }
}