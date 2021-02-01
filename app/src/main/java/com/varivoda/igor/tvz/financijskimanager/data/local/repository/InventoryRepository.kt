package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.InventoryItem
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseInventoryRepository
import com.varivoda.igor.tvz.financijskimanager.model.InventoryDTO
import com.varivoda.igor.tvz.financijskimanager.model.ProductStockDTO

class InventoryRepository(private val database: AppDatabase): BaseInventoryRepository {

    override fun getInventoryItems(): List<InventoryItem> {
        return database.inventoryDao.getInventoryItems()
    }

    override fun getStockData(): List<ProductStockDTO> {
        return database.stockDao.getAllProductsStockData("")
    }

    override fun changeStockData(list: List<InventoryDTO>) {
        list.forEach {
            database.stockDao.changeStockValue(it.productId, it.quantity)
        }

    }


}