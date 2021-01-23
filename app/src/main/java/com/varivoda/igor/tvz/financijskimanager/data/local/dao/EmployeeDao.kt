package com.varivoda.igor.tvz.financijskimanager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Employee
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.model.EmployeeDTO
import com.varivoda.igor.tvz.financijskimanager.model.EmployeeProductDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    @Query("SELECT * FROM Employee")
    fun getEmployees() : Flow<List<Employee>>

    @Query("SELECT * FROM Employee")
    fun getAll() : List<Employee>

    @Query("SELECT e.id, e.employeeName, e.employeeLastName,e.address, e.storeId, e.locationId, s.storeName FROM Employee e INNER JOIN STORE s ON s.id == storeId")
    fun getEmployeesAndStores(): Flow<List<EmployeeDTO>>

    @Query("DELETE FROM Employee WHERE id = :id")
    fun deleteEmployee(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEmployee(employee: Employee)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllEmployees(list: List<Employee>)


    @Query("""SELECT x.employeeName || ' ' || x.employeeLastName || ' je uprihodio/la ' || CAST(total as TEXT) FROM(SELECT e.id,e.employeeName,e.employeeLastName,SUM(pob.quantity*p.price) AS total 
            FROM Employee e JOIN Bill b ON e.id = b.employeeId JOIN ProductsOnBill pob 
            ON pob.billId = b.id JOIN Product p ON p.id = pob.productId 
            WHERE strftime('%m',b.date)= :month AND strftime('%Y',b.date)= :year  
            GROUP BY e.id,e.employeeName,e.employeeLastName 
            ORDER BY SUM(pob.quantity*p.price) DESC LIMIT 1) AS x""")
    fun getEmployeeMostTotalPerMonthAndYear(month: String, year: String): String?

    @Query(
        """select x.firstAndLastName || ' je izdao/la račun u ' || x.counter || ' od 365 dana u godini ' || :year || '.' from 
                (select e.id,e.employeeName || ' ' || e.employeeLastName as firstAndLastName, COUNT(distinct b.date) as counter from 
                Bill b join Employee e on b.employeeId = e.id 
                where strftime('%Y',b.date) = :year 
                group by e.id,e.employeeName || ' ' || e.employeeLastName 
                order by COUNT(distinct b.date) desc limit 1) as x"""
    )
    fun getEmployeeMostDaysIssuedInvoice(year: String): String?

    @Query("""select COUNT(b.id) from Bill b
        WHERE strftime('%m',b.date) = :month and strftime('%Y',b.date) = :year
        and b.employeeId = :id
    """)
    fun getEmployeeTotalInvoicesPerMonth(month: String, year: String, id: Int): String?

    @Query(
        """select x.employeeName, x.employeeLastName, x.num  FROM 
                (SELECT e.id, e.employeeName, e.employeeLastName, SUM(pob.quantity) as num 
                            FROM Employee e JOIN Bill b ON e.id = b.employeeId JOIN ProductsOnBill pob
                ON b.id = pob.billId JOIN Product pr ON pr.id =pob.productId 
                            WHERE strftime('%m',b.date)= :month and strftime('%Y',b.date) = :year and pr.id = :productId
                GROUP BY e.id, e.employeeName, e.employeeLastName 
                           ORDER BY SUM(pob.quantity) DESC LIMIT 1) AS x;"""
    )
    fun employeeMostProductSell(
        month: String,
        year: String,
        productId: Int
    ): EmployeeProductDTO?

    @Query("""SELECT AVG(pob.quantity) FROM Bill b JOIN ProductsOnBill pob on b.id = pob.billId where strftime('%m',b.date)= :month 
        and strftime('%Y',b.date) = :year and b.employeeId = :employeeId""")
    fun getEmployeeAvgKoefPerMonthAndYear(month: String, year: String, employeeId: Int): String?

    @Query("""SELECT AVG(pob.quantity) FROM Bill b JOIN ProductsOnBill pob on b.id = pob.billId where strftime('%m',b.date)= :month 
        and strftime('%Y',b.date) = :year and b.employeeId = :employeeId and b.storeId = :id""")
    fun getEmployeeAvgKoefPerMonthAndYearWithStore(month: String, year: String, employeeId: Int, id: Int): String?

    @Query("""SELECT AVG(pob.quantity) FROM Bill b JOIN ProductsOnBill pob on b.id = pob.billId where 
        strftime('%Y',b.date) = :year and b.employeeId = :employeeId""")
    fun getEmployeeAvgKoefPerYear( year: String, employeeId: Int): String?

    @Query("""SELECT AVG(pob.quantity) FROM Bill b JOIN ProductsOnBill pob on b.id = pob.billId where 
        strftime('%Y',b.date) = :year and b.employeeId = :employeeId and b.storeId = :id""")
    fun getEmployeeAvgKoefPerYearWithStore( year: String, employeeId: Int, id: Int): String?
}