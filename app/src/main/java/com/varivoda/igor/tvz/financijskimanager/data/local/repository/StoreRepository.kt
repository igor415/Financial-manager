package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Location
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import com.varivoda.igor.tvz.financijskimanager.model.*
import com.varivoda.igor.tvz.financijskimanager.util.CustomPeriod
import com.varivoda.igor.tvz.financijskimanager.util.getMonthWithZero
import kotlinx.coroutines.flow.Flow
import java.util.*

class StoreRepository (private val database: AppDatabase) :
    BaseStoreRepository {

    override fun getStores(): Flow<List<Store>> {
        return database.storesDao.getStores()
    }

    override fun getAllStores(): List<Store> {
        return database.storesDao.getAllStores()
    }

    override fun storeTotalPerYear(month: String, year: String): List<PieChartEntry> {
        val list = mutableListOf<PieChartEntry>()
        val allStores = database.storesDao.getAllStores()
        if(month == "-1"){
            val totalPerYear = database.productDao.totalPerYear(year)
            for (i in allStores.indices){
                val num = database.storesDao.storeTotalPerYear(year, allStores[i].id)
                if(num != null && totalPerYear != null){
                    val percent = String.format(Locale.getDefault(), "%.2f", num.toFloat() / totalPerYear.toFloat() * 100)
                    list.add(PieChartEntry(allStores[i].storeName!!,percent))
                }else{
                    list.add(PieChartEntry(allStores[i].storeName!!,"0"))
                }
            }
        }else{
            val total = database.productDao.totalPerYearAndMonth(month, year)
            for (i in allStores.indices){
                val num = database.storesDao.storeTotalPerMonthAndYear(month, year, allStores[i].id)
                if(num != null && total != null){
                    val percent = String.format(Locale.getDefault(), "%.2f", num.toFloat() / total.toFloat() * 100)
                    list.add(PieChartEntry(allStores[i].storeName!!,percent))
                }else{
                    list.add(PieChartEntry(allStores[i].storeName!!,"0"))
                }
            }
        }

        list.forEach {
            it.total = it.total?.replace(",",".")
        }
        return list
    }

    override fun storeBestSellProduct(month: String, year: String, productId: Int, productName: String): String {
        val result = database.storesDao.storeBestSellProduct(month, year, productId) ?: return ""
        return "$result $productName unutar izabranog vremenskog razdoblja."
    }

    override fun getAllLocations(): List<Location> {
        return database.locationDao.getAllLocations()
    }

    override fun getAttendanceForPeriod(enum: CustomPeriod, year: String): List<AttendanceForStore> {
        return when(enum){
            CustomPeriod.CHRISTMAS -> getChristmasData(year).sortedByDescending{ it.number }.take(5)
            CustomPeriod.BLACK_FRIDAY -> getBlackFridayData(year).sortedByDescending{ it.number }.take(5)
            CustomPeriod.EASTER -> getEasterData(year).sortedByDescending{ it.number }.take(5)
            CustomPeriod.SCHOOL_START -> getSchoolData(year).sortedByDescending{ it.number }.take(5)
        }
    }

    override fun getChartDataForTimeOfDay(month: String, year: String, store: Store): List<TimeOfDayData> {
        val timeOfDayData = mutableListOf<TimeOfDayData>()
        if(store.id != -1) {
            timeOfDayData.add(
                TimeOfDayData(
                    "10h-12h",
                    database.storesDao.getAttendanceForTimeOfDay(month, year, store.id, "10", "8")
                )
            )
            timeOfDayData.add(
                TimeOfDayData(
                    "12h-14h",
                    database.storesDao.getAttendanceForTimeOfDay(month, year, store.id, "12", "10")
                )
            )
            timeOfDayData.add(
                TimeOfDayData(
                    "14h-16h",
                    database.storesDao.getAttendanceForTimeOfDay(month, year, store.id, "14", "12")
                )
            )
            timeOfDayData.add(
                TimeOfDayData(
                    "16h-18h",
                    database.storesDao.getAttendanceForTimeOfDay(month, year, store.id, "16", "14")
                )
            )

        }else{
            timeOfDayData.add(
                TimeOfDayData(
                    "08-10h",
                    database.storesDao.getAttendanceForTimeOfDayWithoutStore(month, year, "10", "08")
                )
            )
            timeOfDayData.add(
                TimeOfDayData(
                    "10h-12h",
                    database.storesDao.getAttendanceForTimeOfDayWithoutStore(month, year, "12", "10")
                )
            )
            timeOfDayData.add(
                TimeOfDayData(
                    "12h-14h",
                    database.storesDao.getAttendanceForTimeOfDayWithoutStore(month, year, "14", "12")
                )
            )
            timeOfDayData.add(
                TimeOfDayData(
                    "14h-16h",
                    database.storesDao.getAttendanceForTimeOfDayWithoutStore(month, year, "16", "14")
                )
            )
        }
        return timeOfDayData
    }

    override fun getAllProductsStockData(s: String): List<ProductStockDTO> {
        val data = database.stockDao.getAllProductsStockData(s)
        val cal = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)+1
        data.forEach {
            var num = 0
            for( i in cal until cal+1){
                num += database.billDao.getQuantityOfProduct(it.productId,"01",i.toString())
                num += database.billDao.getQuantityOfProduct(it.productId,"02",i.toString())
                num += database.billDao.getQuantityOfProduct(it.productId,"03",i.toString())
                num += database.billDao.getQuantityOfProduct(it.productId,"04",i.toString())
                num += database.billDao.getQuantityOfProduct(it.productId,"05",i.toString())
                num += database.billDao.getQuantityOfProduct(it.productId,"06",i.toString())
                num += database.billDao.getQuantityOfProduct(it.productId,"07",i.toString())
                num += database.billDao.getQuantityOfProduct(it.productId,"08",i.toString())
                num += database.billDao.getQuantityOfProduct(it.productId,"09",i.toString())
                num += database.billDao.getQuantityOfProduct(it.productId,"10",i.toString())
                num += database.billDao.getQuantityOfProduct(it.productId,"11",i.toString())
                num += database.billDao.getQuantityOfProduct(it.productId,"12",i.toString())
            }
            val avg = num/12
            if(avg > it.quantity){
                it.yellow = true
            }
        }
        return data
    }

    override fun getInfo(productId: Int): List<ProductStockDTO> {
        return database.stockDao.getInfo(productId)
    }

    override fun getNumberForStoreId(id: Int): String? {
        return database.storesDao.getNumberForStoreId(id)
    }

    private fun getEasterData(year: String): MutableList<AttendanceForStore> {
        val all = database.storesDao.getAllStores()
        val result = mutableListOf<AttendanceForStore>()
        all.forEach {
            var total = 0
            for (i in 1 until 32){
                total += if(i < 10){
                    database.storesDao.getAttendanceForPeriod(it.id,year,"04", getMonthWithZero(i))
                }else{
                    database.storesDao.getAttendanceForPeriod(it.id,year,"04",i.toString())
                }
            }
            result.add(AttendanceForStore(it.storeName!!,total))
        }
        return result
    }

    private fun getBlackFridayData(year: String): MutableList<AttendanceForStore> {
        val all = database.storesDao.getAllStores()
        val result = mutableListOf<AttendanceForStore>()
        all.forEach {
            var total = 0
            for (i in 27 until 31){
                val prvo = database.storesDao.getAttendanceForPeriod(it.id, year, "11", i.toString())
                total += prvo
            }
            result.add(AttendanceForStore(it.storeName!!,total))
        }
        return result
    }

    private fun getSchoolData(year: String): MutableList<AttendanceForStore>{
        val all = database.storesDao.getAllStores()
        val result = mutableListOf<AttendanceForStore>()
        all.forEach {
            var total = 0
                for (i in 15 until 32){
                    total += database.storesDao.getAttendanceForPeriod(it.id,year,"08",i.toString())
                }

            for (i in 1 until 16){
                total += if(i < 10){
                    database.storesDao.getAttendanceForPeriod(it.id,year,"09", getMonthWithZero(i))
                }else{
                    database.storesDao.getAttendanceForPeriod(it.id,year,"09",i.toString())
                }

            }

            result.add(AttendanceForStore(it.storeName!!,total))
        }

        return result
    }

    private fun getChristmasData(year: String): MutableList<AttendanceForStore>{
        val all = database.storesDao.getAllStores()
        val result = mutableListOf<AttendanceForStore>()
        all.forEach {
            var total = 0
                for (i in 15 until 32){
                    total += database.storesDao.getAttendanceForPeriod(it.id,year,"12",i.toString())
                }
            result.add(AttendanceForStore(it.storeName!!,total))
        }
        return result
    }
}