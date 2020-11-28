package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Employee
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseEmployeeRepository
import com.varivoda.igor.tvz.financijskimanager.model.EmployeeDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeEmployeeRepository :
    BaseEmployeeRepository {
    override fun getEmployees(): Flow<List<Employee>> {
        return flow { listOf(Employee(1,"ivica","prezime",null,1,1)) }
    }

    override fun getEmployeesAndStores(): Flow<List<EmployeeDTO>> {
        return flow { listOf(EmployeeDTO(1,"ivo","zadro","hrvatska")) }
    }

    override fun deleteEmployee(id: Int) {

    }

    override suspend fun getEmployeeTotalPerMonthAndYear(month: String, year: String): String? {
        return "string"
    }

    override fun getEmployeeMostDaysIssuedInvoice(year: String): String? {
        return "string"
    }

}