package com.varivoda.igor.tvz.financijskimanager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Category
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.model.CategoryDTO

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCategories(list: List<Category>)

    @Query("""SELECT SUM(pob.quantity) FROM Product p JOIN ProductsOnBill pob on pob.productId = p.id
        JOIN Bill b on b.id = pob.billId where strftime('%m',b.date) = :month 
                and strftime('%Y',b.date) = :year and p.categoryId = :id
    """)
    fun getTop3CategoriesAtLeastSoldInYearAndMonth(month: String, year: String, id: Int): Int

    @Query("""SELECT SUM(pob.quantity) FROM Product p JOIN ProductsOnBill pob on pob.productId = p.id
        JOIN Bill b on b.id = pob.billId where strftime('%Y',b.date) = :year and p.categoryId = :id
    """)
    fun getTop3CategoriesAtLeastSoldInOnlyYear(year: String, id: Int): Int

    @Query("""SELECT SUM(pob.quantity) FROM Product p JOIN ProductsOnBill pob on pob.productId = p.id
        JOIN Bill b on b.id = pob.billId where strftime('%m',b.date) = :month 
                and strftime('%Y',b.date) = :year and p.categoryId = :id and b.storeId = :storeId
    """)
    fun getTop3CategoriesAtLeastSoldInYearAndMonthWithStore(month: String, year: String, id: Int, storeId: Int): Int

    @Query("""SELECT SUM(pob.quantity) FROM Product p JOIN ProductsOnBill pob on pob.productId = p.id
        JOIN Bill b on b.id = pob.billId where strftime('%Y',b.date) = :year and p.categoryId = :id and b.storeId = :storeId
    """)
    fun getTop3CategoriesAtLeastSoldInOnlyYearWithStore(year: String, id: Int, storeId: Int): Int

    @Query("SELECT * FROM Category")
    fun getAllCategories(): List<Category>
}