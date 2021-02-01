package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Employee
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.data.local.remote.Api
import com.varivoda.igor.tvz.financijskimanager.data.local.remote.ApiService
import com.varivoda.igor.tvz.financijskimanager.data.local.remote.model.LoginEntry
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseEmployeeRepository
import com.varivoda.igor.tvz.financijskimanager.model.EmployeeDTO
import com.varivoda.igor.tvz.financijskimanager.model.HorizontalBarChartEntry
import com.varivoda.igor.tvz.financijskimanager.monitoring.ConnectivityAgent
import com.varivoda.igor.tvz.financijskimanager.util.NetworkResult
import com.varivoda.igor.tvz.financijskimanager.util.toSha2
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

class EmployeeRepository(private val appDatabase: AppDatabase, private val connectivityAgent: ConnectivityAgent) :
    BaseEmployeeRepository {

    val api = Api.retrofitService

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

    override fun getHorizontalBarChartData(month: String, year: String, storeId: Int): List<HorizontalBarChartEntry> {
        val allEmployees = appDatabase.employeeDao.getAll()
        val list = mutableListOf<HorizontalBarChartEntry>()
        if(storeId == -1){
            if(month != "-1"){
                allEmployees.forEach {
                    val x = appDatabase.employeeDao.getEmployeeAvgKoefPerMonthAndYear(month, year, it.id)
                    if(x != null){
                        list.add(HorizontalBarChartEntry("  ${it.employeeName.first()}. ${it.employeeLastName}", x.toFloat()))
                    }else{
                        list.add(HorizontalBarChartEntry("  ${it.employeeName.first()}. ${it.employeeLastName}", 0f))
                    }
                }

                list.sortBy { it.totalInvoice }
                return list.reversed().take(3)
            }else{
                allEmployees.forEach {
                    val x = appDatabase.employeeDao.getEmployeeAvgKoefPerYear(year, it.id)
                    if(x != null){
                        list.add(HorizontalBarChartEntry("  ${it.employeeName.first()}. ${it.employeeLastName}", x.toFloat()))
                    }else{
                        list.add(HorizontalBarChartEntry("  ${it.employeeName.first()}. ${it.employeeLastName}", 0f))
                    }
                }

                list.sortBy { it.totalInvoice }
                return list.reversed().take(3)
            }
        }else{
            if(month != "-1"){
                allEmployees.forEach {
                    val x = appDatabase.employeeDao.getEmployeeAvgKoefPerMonthAndYearWithStore(month, year, it.id, storeId)
                    if(x != null){
                        list.add(HorizontalBarChartEntry("  ${it.employeeName.first()}. ${it.employeeLastName}", x.toFloat()))
                    }else{
                        list.add(HorizontalBarChartEntry("  ${it.employeeName.first()}. ${it.employeeLastName}", 0f))
                    }
                }

                list.sortBy { it.totalInvoice }
                return list.reversed().take(3)
            }else{
                allEmployees.forEach {
                    val x = appDatabase.employeeDao.getEmployeeAvgKoefPerYearWithStore(year, it.id, storeId)
                    if(x != null){
                        list.add(HorizontalBarChartEntry("  ${it.employeeName.first()}. ${it.employeeLastName}", x.toFloat()))
                    }else{
                        list.add(HorizontalBarChartEntry("  ${it.employeeName.first()}. ${it.employeeLastName}", 0f))
                    }
                }

                list.sortBy { it.totalInvoice }
                return list.reversed().take(3)
            }
        }

        /*val splitted = dateSelected.split(".")
        val allEmployees = appDatabase.employeeDao.getAll()
        val list = mutableListOf<HorizontalBarChartEntry>()
        allEmployees.forEach {
            val x = appDatabase.employeeDao.getEmployeeTotalInvoicesPerMonth(splitted[0],splitted[1],it.id)
            if(x != null){
                list.add(HorizontalBarChartEntry("  ${it.employeeName.first()}. ${it.employeeLastName}", x.toFloat()))
            }else{
                list.add(HorizontalBarChartEntry("  ${it.employeeName.first()}. ${it.employeeLastName}", 0f))
            }
        }
        list.sortBy { it.totalInvoice }
        return list*/
    }

    override fun employeeMostProductSell(dateSelected: String, product: Product): String {
        val splitted = dateSelected.split(".")
        val result = appDatabase.employeeDao.employeeMostProductSell(splitted[0],splitted[1],product.id)
        return if(result!=null) {
            if (result.employeeLastName != null && result.employeeName != null && result.num != null) {
                "Zaposlenik ${result.employeeName} ${result.employeeLastName} je prodao proizvod ${product.productName} u količini ${result.num}."
            } else {
                ""
            }
        }else{
            ""
        }

    }

    override fun changeEmployeeInfo(token: String, employee: Employee): NetworkResult<Boolean> {
        if(connectivityAgent.isDeviceConnectedToInternet) {
            return try {
                val response = api.changeEmployeeInfo(token, employee).execute()
                when(response.code()){
                    200 -> {
                        NetworkResult.Success(true)
                    }
                    else -> NetworkResult.Error(Exception(response.message()))
                }
            }catch (ex: Exception){
                NetworkResult.Error(ex)
            }

        }else{
            return NetworkResult.NoNetworkConnection("")
        }
    }
}