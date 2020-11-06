package com.varivoda.igor.tvz.financijskimanager.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM Product")
    fun getAllProducts(): Flow<List<Product>>

}