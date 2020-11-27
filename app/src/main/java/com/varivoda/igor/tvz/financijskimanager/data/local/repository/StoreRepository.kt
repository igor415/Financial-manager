package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import kotlinx.coroutines.flow.Flow

class StoreRepository (private val database: AppDatabase) :
    BaseStoreRepository {

    override fun getStores(): Flow<List<Store>> {
        return database.storesDao.getStores()
    }
}