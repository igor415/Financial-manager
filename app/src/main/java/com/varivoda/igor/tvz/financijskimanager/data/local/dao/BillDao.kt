package com.varivoda.igor.tvz.financijskimanager.data.local.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Bill
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.PaymentMethod
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.model.BillDTO
import com.varivoda.igor.tvz.financijskimanager.model.DataOnBill
import com.varivoda.igor.tvz.financijskimanager.model.EmployeeBestSale

@Dao
interface BillDao {

    @Query("""select x.id as id, x.money as money, x.date as date, x.storeName as storeName  from 
            (select b.id,b.date, sum(pob.quantity*p.price) as money, s.storeName 
            from Bill b join ProductsOnBill pob on b.id = pob.billId
            join Product p on p.id = pob.productId
            join Store s on b.storeId = s.id
            where strftime('%m',b.date)=:month and strftime('%Y',b.date)=:year 
            group by b.id,b.date order by money desc) as x""")
    fun getBills(month: String, year: String): DataSource.Factory<Int,BillDTO>

    @Query("SELECT * FROM Bill")
    fun get(): List<Bill>

    @Insert
    fun insertBill(bill: Bill)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllBills(list: List<Bill>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPaymentMethods(list: List<PaymentMethod>)

    @Query("SELECT SUM(pob.quantity) FROM Bill b join ProductsOnBill pob on b.id = pob.billId where pob.productId = :id and strftime('%m',b.date)=:month and strftime('%Y',b.date)=:year ")
    fun getQuantityOfProduct(id: Int, month: String, year: String): Int

    @Query("SELECT * FROM PaymentMethod")
    fun getAllPaymentMethods(): List<PaymentMethod>

    @Query("""SELECT SUM(pob.quantity*p.price) FROM Bill b join ProductsOnBill pob on pob.billId = b.id
         join Product p on p.id = pob.productId where b.paymentMethodId = :paymentMethodId and strftime('%m',b.date)=:month 
        and strftime('%Y',b.date)=:year and b.storeId = :storeId""")
    fun getPaymentMethodTotal(paymentMethodId: Int, month: String, year: String, storeId: Int): Double?

    @Query("""SELECT SUM(pob.quantity*p.price) FROM Bill b join ProductsOnBill pob on pob.billId = b.id
         join Product p on p.id = pob.productId where b.paymentMethodId = :paymentMethodId 
        and strftime('%Y',b.date)=:year and b.storeId = :storeId""")
    fun getPaymentMethodTotalWithoutMonth(paymentMethodId: Int, year: String, storeId: Int): Double?

    @Query("""SELECT SUM(pob.quantity*p.price) FROM Bill b join ProductsOnBill pob on pob.billId = b.id
         join Product p on p.id = pob.productId where b.paymentMethodId = :paymentMethodId and strftime('%m',b.date)=:month 
        and strftime('%Y',b.date)=:year """)
    fun getPaymentMethodTotalWithoutStore(paymentMethodId: Int, month: String, year: String): Double?

    @Query("""SELECT SUM(pob.quantity*p.price) FROM Bill b join ProductsOnBill pob on pob.billId = b.id
         join Product p on p.id = pob.productId where b.paymentMethodId = :paymentMethodId 
        and strftime('%Y',b.date)=:year """)
    fun getPaymentMethodTotalWithoutMonthWithoutStore(paymentMethodId: Int, year: String): Double?

    @Query("""SELECT b.id as invoiceId,e.id as employeeId,e.employeeName as name, e.employeeLastName as surname
         , SUM(pob.quantity*p.price) as total, pm.name as paymentMethodName, b.date, b.time, s.storeName FROM Bill b join ProductsOnBill pob on b.id = pob.billId
         join Product p on p.id = pob.productId join Employee e on e.id = b.employeeId join Store s on s.id = b.storeId
    join PaymentMethod pm on pm.id = b.paymentMethodId where strftime('%m',b.date)=:month 
                and strftime('%Y',b.date)=:year and b.storeId = :storeId group by b.id order by SUM(pob.quantity*p.price) desc limit 1""")
    fun getEmployeeWithBestSaleInvoice(month: String, year: String, storeId: Int): EmployeeBestSale

    @Query("""SELECT b.id as invoiceId,e.id as employeeId,e.employeeName as name, e.employeeLastName as surname
         , SUM(pob.quantity*p.price) as total, pm.name as paymentMethodName, b.date, b.time, s.storeName FROM Bill b join ProductsOnBill pob on b.id = pob.billId
         join Product p on p.id = pob.productId join Employee e on e.id = b.employeeId join Store s on s.id = b.storeId
    join PaymentMethod pm on pm.id = b.paymentMethodId where strftime('%m',b.date)=:month 
                and strftime('%Y',b.date)=:year group by b.id order by SUM(pob.quantity*p.price) desc limit 1""")
    fun getEmployeeWithBestSaleInvoiceWithoutStore(month: String, year: String): EmployeeBestSale


    @Query("""SELECT b.id as invoiceId,e.id as employeeId,e.employeeName as name, e.employeeLastName as surname
         , SUM(pob.quantity*p.price) as total, pm.name as paymentMethodName, b.date, b.time, s.storeName FROM Bill b join ProductsOnBill pob on b.id = pob.billId
         join Product p on p.id = pob.productId join Employee e on e.id = b.employeeId join Store s on s.id = b.storeId
    join PaymentMethod pm on pm.id = b.paymentMethodId and b.id = :invoice""")
    fun getInvoiceInfo(invoice: Int): EmployeeBestSale?

    @Query("SELECT p.id, pb.quantity, p.productName, 0 as selected FROM ProductsOnBill pb JOIN Product p on pb.productId = p.id WHERE pb.billId = :id")
    fun getDataOnBill(id: Int): List<DataOnBill>
}