package com.varivoda.igor.tvz.financijskimanager.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.varivoda.igor.tvz.financijskimanager.model.ProductStockDTO

@Dao
interface StockDao {

    @Query("SELECT p.id, p.productName, s.quantity, s.idStore FROM stockdata s JOIN Product p on s.productId = p.id where s.idStore = 1 AND (p.productName LIKE '%' || :s || '%') order by s.quantity asc")
    fun getAllProductsStockData(s: String): List<ProductStockDTO>

    @Query("SELECT p.id, st.storeName as productName, s.quantity, s.idStore  FROM stockdata s JOIN Product p on s.productId = p.id join Store st on st.id = s.idStore where s.idStore != 1 and p.id = :id")
    fun getInfo(id: Int): List<ProductStockDTO>
}