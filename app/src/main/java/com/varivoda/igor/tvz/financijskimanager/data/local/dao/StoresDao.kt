package com.varivoda.igor.tvz.financijskimanager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.model.AttendanceForStore
import com.varivoda.igor.tvz.financijskimanager.model.ProductStockDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface StoresDao {

    @Query("SELECT * FROM Store")
    fun getStores(): Flow<List<Store>>

    @Query("SELECT * FROM Store")
    fun getAllStores(): List<Store>

    @Insert
    fun insertStore(store: Store)

    @Query(
        """
                SELECT SUM(pob.quantity*p.price) as profit 
                FROM Bill b JOIN ProductsOnBill pob ON b.id = pob.billId
                JOIN product p ON p.id = pob.productId
                WHERE strftime('%Y',b.date) = :year 
                and b.storeId = :id """
    )
    fun storeTotalPerYear(year: String?, id: Int): String?

    @Query(
        """
                SELECT SUM(pob.quantity*p.price) as profit 
                FROM Bill b JOIN ProductsOnBill pob ON b.id = pob.billId
                JOIN product p ON p.id = pob.productId
                WHERE strftime('%Y',b.date) = :year and strftime('%m',b.date) = :month 
                and b.storeId = :id """
    )
    fun storeTotalPerMonthAndYear(month: String, year: String?, id: Int): String?

    @Query(
        """select 'Poslovnica ' || x.storeName || ' je ostvarila profit od ' || x.profit || ' kn prodajom proizvoda' from 
                (select s.storeName, SUM(pob.quantity*p.price) as profit 
                from Bill b join Store s on b.storeId = s.id join ProductsOnBill 
                pob on pob.billId = b.id join Product p on p.id = pob.productId 
                where p.id = :productId and strftime('%m',b.date)=:month 
                and strftime('%Y',b.date) = :year 
                group by s.storeName order by SUM(pob.quantity*p.price) desc limit 1) as x;"""
    )
    fun storeBestSellProduct(
        month: String,
        year: String,
        productId: Int
    ): String?

    @Query("""SELECT COUNT(b.id) FROM Bill b WHERE b.storeId = :id and strftime('%Y',b.date) = :year
        and strftime('%m',b.date) = :month and strftime('%d',b.date) = :day
    """)
    fun getAttendanceForPeriod(id: Int, year: String, month: String, day: String): Int

    @Query(""" SELECT COUNT(b.id) FROM Bill b WHERE b.storeId = :id and strftime('%Y',b.date) = :year
        and strftime('%m',b.date) = :month and strftime('%H',b.time) >= :bottomValue and strftime('%H',b.time) < :topValue""")
    fun getAttendanceForTimeOfDay(month: String, year: String, id: Int, topValue: String, bottomValue: String): Int

    @Query(""" SELECT COUNT(b.id) FROM Bill b WHERE strftime('%Y',b.date) = :year
        and strftime('%m',b.date) = :month and strftime('%H',b.time) >= :bottomValue and strftime('%H',b.time) < :topValue""")
    fun getAttendanceForTimeOfDayWithoutStore(month: String, year: String, topValue: String, bottomValue: String): Int

    @Query("SELECT COUNT(*) FROM Bill")
    fun getBillCount(): Int

    @Query("SELECT phoneNumber from Store where id = :id")
    fun getNumberForStoreId(id: Int): String?
}