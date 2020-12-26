package com.varivoda.igor.tvz.financijskimanager.data.local.repository.base

import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Location
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.model.AttendanceForStore
import com.varivoda.igor.tvz.financijskimanager.model.PieChartEntry
import com.varivoda.igor.tvz.financijskimanager.model.ProductStockDTO
import com.varivoda.igor.tvz.financijskimanager.model.TimeOfDayData
import com.varivoda.igor.tvz.financijskimanager.util.CustomPeriod
import kotlinx.coroutines.flow.Flow

interface BaseStoreRepository {
    fun getStores(): Flow<List<Store>>
    fun getAllStores(): List<Store>
    fun storeTotalPerYear(year: String): List<PieChartEntry>
    fun storeBestSellProduct(
        month: String,
        year: String,
        productId: Int,
        productName: String
    ): String
    fun getAllLocations(): List<Location>
    fun getAttendanceForPeriod(enum: CustomPeriod, year: String): List<AttendanceForStore>
    fun getChartDataForTimeOfDay(month: String, year: String, store: Store): List<TimeOfDayData>
    fun getAllProductsStockData(s: String): List<ProductStockDTO>
    fun getInfo(productId: Int): List<ProductStockDTO>
    fun getNumberForStoreId(id: Int): String?
}