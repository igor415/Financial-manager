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


    @Query("""SELECT x.employeeName || ' ' || x.employeeLastName || ' je uprihodio/la ' || CAST(total as TEXT) FROM(SELECT e.id,e.employeeName,e.employeeLastName,SUM(pob.quantity*p.price) AS total 
            FROM Employee e JOIN Bill b ON e.id = b.employeeId JOIN ProductsOnBill pob 
            ON pob.billId = b.id JOIN Product p ON p.id = pob.productId 
            WHERE strftime('%m',b.date)= :month AND strftime('%Y',b.date)= :year  
            GROUP BY e.id,e.employeeName,e.employeeLastName 
            ORDER BY SUM(pob.quantity*p.price) DESC LIMIT 1) AS x""")
    fun getEmployeeMostTotalPerMonthAndYear(month: String, year: String): String
}