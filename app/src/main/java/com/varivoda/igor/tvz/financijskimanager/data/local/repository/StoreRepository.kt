package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import kotlinx.coroutines.flow.Flow

class StoreRepository (private val database: AppDatabase){

    fun getStores(): Flow<List<Store>> {
        return database.storesDao.getStores()
    }
}