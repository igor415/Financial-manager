package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.InventoryItem
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseInventoryRepository

class InventoryRepository(private val database: AppDatabase): BaseInventoryRepository {

    override fun getInventoryItems(): List<InventoryItem> {
        return database.inventoryDao.getInventoryItems()
    }


}