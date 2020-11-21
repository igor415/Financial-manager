package com.varivoda.igor.tvz.financijskimanager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import kotlinx.coroutines.flow.Flow

@Dao
interface StoresDao {

    @Query("SELECT * FROM Store")
    fun getStores(): Flow<List<Store>>

    @Insert
    fun insertStore(store: Store)
}