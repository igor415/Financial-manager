package com.varivoda.igor.tvz.financijskimanager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.ProductsOnBill
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store

@Dao
interface ProductOnBillDao {

    @Insert
    fun insertProductOnBill(productsOnBill: ProductsOnBill)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllProductsOnBill(list: List<ProductsOnBill>)
}