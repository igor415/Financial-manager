package com.varivoda.igor.tvz.financijskimanager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.StockData
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.model.ProductStockDTO

@Dao
interface StockDao {

    @Query("SELECT p.id, p.productName, s.quantity, s.idStore, 0 as yellow  FROM stockdata s JOIN Product p on s.productId = p.id where s.idStore = 1 AND (p.productName LIKE '%' || :s || '%') order by s.quantity asc")
    fun getAllProductsStockData(s: String): List<ProductStockDTO>

    @Query("SELECT * FROM StockData WHERE idStore = 1")
    fun getAll(): List<StockData>

    @Query("SELECT p.id, st.storeName as productName, s.quantity, s.idStore, 0 as yellow  FROM stockdata s JOIN Product p on s.productId = p.id join Store st on st.id = s.idStore where s.idStore != 1 and p.id = :id")
    fun getInfo(id: Int): List<ProductStockDTO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllStocks(list: List<StockData>)

    @Query("UPDATE StockData set quantity = :quantity where productId = :productId and idStore = 1")
    fun changeStockValue(productId: Int, quantity: Int)

    @Query("SELECT quantity FROM StockData where productId = :productId and idStore = 1")
    fun getTotalQuantity(productId: Int): Int?

}