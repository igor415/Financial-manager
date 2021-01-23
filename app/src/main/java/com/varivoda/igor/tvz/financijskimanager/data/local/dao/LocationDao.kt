package com.varivoda.igor.tvz.financijskimanager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.County
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Location

@Dao
interface LocationDao {

    @Insert
    fun insertLocation(location: Location)

    @Query("SELECT * FROM Location")
    fun getAllLocations(): List<Location>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllLocations(list: List<Location>)

}