package com.varivoda.igor.tvz.financijskimanager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.ProductsOnBill

@Dao
interface ProductOnBillDao {

    @Insert
    fun insertProductOnBill(productsOnBill: ProductsOnBill)
}