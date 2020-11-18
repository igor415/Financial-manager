package com.varivoda.igor.tvz.financijskimanager.data.local.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.model.ProductQuarterDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM Product")
    fun getAllProducts(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: Product)

    @Query("DELETE FROM Product WHERE id = :id")
    fun deleteProduct(id: Int)

    @Query(
        """select x.quarter,x.productName,cast(max(x.total) as text) as number from 
                (select case when strftime('%m',b.date) between '01' and '03' then 'first' 
                when strftime('%m',b.date) between '04' and '06' then 'second' 
                when strftime('%m',b.date) between '07' and '09' then 'third' 
                else 'fourth' end as quarter 
                , p.productName,SUM(pob.quantity) as total 
                from Bill b join ProductsOnBill pob on b.id = pob.billId 
                join Product p on p.id = pob.productId where strftime('%Y',b.date) = :year 
                group by quarter,p.productName) as x 
                group by quarter"""
    )
    fun productPerQuarter(year: String): List<ProductQuarterDTO>
}