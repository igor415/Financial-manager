package com.varivoda.igor.tvz.financijskimanager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.model.PieChartEntry
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
}