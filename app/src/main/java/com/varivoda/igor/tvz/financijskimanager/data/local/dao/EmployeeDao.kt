package com.varivoda.igor.tvz.financijskimanager.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Employee
import com.varivoda.igor.tvz.financijskimanager.model.EmployeeDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    @Query("SELECT * FROM Employee")
    fun getEmployees() : Flow<List<Employee>>

    @Query("SELECT e.id, e.employeeName, e.employeeLastName,s.storeName FROM Employee e INNER JOIN STORE s ON s.id == storeId")
    fun getEmployeesAndStores(): Flow<List<EmployeeDTO>>

    @Query("DELETE FROM Employee WHERE id = :id")
    fun deleteEmployee(id: Int)
}