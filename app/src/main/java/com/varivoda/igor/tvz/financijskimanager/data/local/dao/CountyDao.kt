package com.varivoda.igor.tvz.financijskimanager.data.local.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.County

@Dao
interface CountyDao {

    @Query("SELECT * FROM County")
    fun getCounties(): List<County>

    @Insert
    fun insertCounty(county: County)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCounties(list: List<County>)
}