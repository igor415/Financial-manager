package com.varivoda.igor.tvz.financijskimanager.data.local.repository.base

import com.varivoda.igor.tvz.financijskimanager.data.local.entity.InventoryItem

interface BaseInventoryRepository {
    fun getInventoryItems(): List<InventoryItem>

}