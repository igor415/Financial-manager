package com.varivoda.igor.tvz.financijskimanager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
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
}