package com.varivoda.igor.tvz.financijskimanager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Location

@Dao
interface LocationDao {

    @Insert
    fun insertLocation(location: Location)
}