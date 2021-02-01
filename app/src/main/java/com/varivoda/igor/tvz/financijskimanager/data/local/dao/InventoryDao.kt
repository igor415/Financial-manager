package com.varivoda.igor.tvz.financijskimanager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.InventoryItem

@Dao
interface InventoryDao {

    @Query("SELECT * FROM InventoryItem")
    fun getInventoryItems(): List<InventoryItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(inventoryItem: InventoryItem)
}