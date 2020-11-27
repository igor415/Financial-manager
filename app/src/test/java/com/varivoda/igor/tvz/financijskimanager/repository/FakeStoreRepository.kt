package com.varivoda.igor.tvz.financijskimanager.repository

import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow

class FakeStoreRepository : BaseStoreRepository{

    override fun getStores(): Flow<List<Store>> {
        return flow {
            mutableListOf<Store>(Store(1,"hrvatska",null,0),
                Store(2,"slovenija",null,0))
        }
    }

}