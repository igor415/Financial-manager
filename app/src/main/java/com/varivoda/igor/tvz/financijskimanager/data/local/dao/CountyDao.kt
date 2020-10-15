package com.varivoda.igor.tvz.financijskimanager.data.local.dao

import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.County

@Dao
interface CountyDao {

    @Query("SELECT * FROM County")
    fun getCounties(): List<County>
}