package com.varivoda.igor.tvz.financijskimanager.data.local.repository.base

import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import kotlinx.coroutines.flow.Flow

interface BaseStoreRepository {
    fun getStores(): Flow<List<Store>>
}