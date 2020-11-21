package com.varivoda.igor.tvz.financijskimanager.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.varivoda.igor.tvz.financijskimanager.data.local.dao.*
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.*
import com.varivoda.igor.tvz.financijskimanager.util.getOrAwaitValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers.closeTo
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var productDao: ProductDao
    private lateinit var billDao: BillDao
    private lateinit var productOnBillDao: ProductOnBillDao
    private lateinit var employeeDao: EmployeeDao
    private lateinit var storesDao: StoresDao
    private lateinit var customerDao: CustomerDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().context
        appDatabase = Room.inMemoryDatabaseBuilder(context,AppDatabase::class.java).allowMainThreadQueries().build()
        productDao = appDatabase.productDao
        billDao = appDatabase.billDao
        productOnBillDao = appDatabase.productOnBillDao
        employeeDao = appDatabase.employeeDao
        storesDao = appDatabase.storesDao
        customerDao = appDatabase.customerDao
        appDatabase.countyDao.insertCounty(County(1,"zup"))
        appDatabase.locationDao.insertLocation(Location(1,null,1))
        storesDao.insertStore(Store(1,"store",null,1))
        customerDao.insertCustomer(Customer(1,null,null,null,0,1))
        employeeDao.insertEmployee(Employee(1,"mirko",null,null,1,1))
    }

    @Test
    fun addProduct(){
        productDao.insertProduct(Product(1,"voda",8.50))
        assertEquals(1,productDao.getProductCount())
    }

    @Test
    fun deleteProduct(){
        productDao.insertProduct(Product(1,"voda",8.50))
        productDao.deleteProduct(1)
        productDao.deleteProduct(1)
        assertThat(productDao.getProductCount(),`is`(equalTo(0)))
    }

    @Test
    fun insertBill(){
        billDao.insertBill(Bill(1,1,1,1,null))
        assertThat(billDao.get().size.toDouble(), `is`(closeTo(1.0,0.01)))
    }

    @Test
    fun getTop10(){
        productDao.insertProduct(Product(1,"voda",8.50))
        billDao.insertBill(Bill(1,1,1,1,"2020-06-01"))
        productOnBillDao.insertProductOnBill(ProductsOnBill(1,1,1,2))
        assertThat(productDao.top10Products("06","2020").getOrAwaitValue()[0].price, `is`(closeTo(2.0,0.01)))
    }

    @Test
    fun productPerQuarter(){
        productDao.insertProduct(Product(1,"voda",8.50))
        billDao.insertBill(Bill(1,1,1,1,"2020-06-01"))
        productOnBillDao.insertProductOnBill(ProductsOnBill(1,1,1,2))
        assertEquals("second",productDao.productPerQuarter("2020")[0].quarter)
    }

    @Test
    fun getCounties(){
        assertEquals("zup",appDatabase.countyDao.getCounties()[0].countyName)
    }

    @Test
    fun getEmployeeMostDaysIssuedInvoice(){
        assertEquals(null,employeeDao.getEmployeeMostDaysIssuedInvoice("2020"))
    }

    @Test
    fun getEmployeeMostTotalPerMonthAndYear(){
        assertEquals(null,employeeDao.getEmployeeMostTotalPerMonthAndYear("06","2020"))
    }

    @After
    @Throws(IOException::class)
    fun closeDB(){
        appDatabase.close()
    }
}