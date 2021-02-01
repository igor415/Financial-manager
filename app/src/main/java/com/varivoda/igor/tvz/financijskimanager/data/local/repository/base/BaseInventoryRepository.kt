package com.varivoda.igor.tvz.financijskimanager.data.local.repository.base

import com.varivoda.igor.tvz.financijskimanager.data.local.entity.InventoryItem
import com.varivoda.igor.tvz.financijskimanager.model.InventoryDTO
import com.varivoda.igor.tvz.financijskimanager.model.ProductStockDTO

interface BaseInventoryRepository {
    fun getInventoryItems(): List<InventoryItem>
    fun getStockData(): List<ProductStockDTO>
    fun changeStockData(list: List<InventoryDTO>)

}