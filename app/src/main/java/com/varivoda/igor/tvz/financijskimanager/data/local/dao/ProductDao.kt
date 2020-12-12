package com.varivoda.igor.tvz.financijskimanager.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
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

    @Query("SELECT * FROM Product ORDER BY categoryId ASC")
    fun getAllProductsPaging(): PagingSource<Int,Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: Product)

    @Query("DELETE FROM Product WHERE id = :id")
    fun deleteProduct(id: Int)

    @Query("SELECT COUNT(*) FROM Product")
    fun getProductCount(): Int

    @Query("SELECT * FROM product")
    fun getProducts(): List<Product>

    @Query("UPDATE Product SET image = :image WHERE id = :id")
    fun updateProductImage(image: String, id: Int)

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

    @Query(
        """SELECT p.id,p.productName,SUM(pob.quantity) as price,p.categoryId 
                FROM Product p JOIN ProductsOnBill pob ON p.id = pob.productId 
                JOIN Bill b ON b.id = pob.billId where strftime('%m',b.date) = :month 
                and strftime('%Y',b.date) = :year 
                GROUP BY p.id,p.productName ORDER BY SUM(pob.quantity) DESC LIMIT 10"""
    )
    fun top10Products(month: String?, year: String?): LiveData<List<Product>>

    @Query("""select 'Račun broj ' || x.id || ' je izdan u poslovnici ' || x.storeName || ' sa brojem stavki = ' || x.counter   from 
                (select b.id,s.id,s.storeName,count(pob.billId) as counter 
                from Bill b join ProductsOnBill pob on b.id = pob.billId 
                join Store s on s.id = b.storeId 
                where strftime('%Y',b.date)=:year 
                group by b.id,s.id,s.storeName 
                order by count(pob.billId) desc limit 1) as x"""
    )
    fun mostItemsOnBill(year: String?): LiveData<String?>

    @Query(
        """
                SELECT SUM(pob.quantity) as quantity 
                FROM Product p JOIN ProductsOnBill pob ON p.id = pob.productId 
                JOIN Bill b ON b.id = pob.billId where strftime('%m',b.date) = :month 
                and strftime('%Y',b.date) = :year and strftime('%d',b.date) = :day and p.id = :id"""
    )
    fun getStatisticsForProduct(day: String, month: String, year: String, id: Int): Int

    @Query(
        """SELECT x.productName || '#' || x.total FROM 
                (SELECT p.id,p.productName,SUM(pob.quantity*p.price) as total 
                FROM Product p JOIN ProductsOnBill pob ON p.id = pob.productId 
                JOIN Bill b ON b.id = pob.billId where strftime('%m',b.date) = :month 
                and strftime('%Y',b.date) = :year 
                GROUP BY p.id,p.productName ORDER BY SUM(pob.quantity*p.price) LIMIT 1 ) as x;"""
    )
    fun productSmallestShare(month: String, year: String): String?

    @Query(
        """SELECT SUM(pob.quantity*p.price) as total 
                FROM Product p JOIN ProductsOnBill pob ON p.id = pob.productId 
                JOIN Bill b ON b.id = pob.billId where strftime('%m',b.date) = :month 
                and strftime('%Y',b.date) = :year """
    )
    fun totalPerMonth(month: String?, year: String?): String?

}