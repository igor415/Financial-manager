package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.data.local.remote.Api
import com.varivoda.igor.tvz.financijskimanager.data.local.remote.model.AllData
import com.varivoda.igor.tvz.financijskimanager.data.local.remote.model.FingerprintEntry
import com.varivoda.igor.tvz.financijskimanager.data.local.remote.model.LoginEntry
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseLoginRepository
import com.varivoda.igor.tvz.financijskimanager.monitoring.ConnectivityAgent
import com.varivoda.igor.tvz.financijskimanager.util.NetworkResult
import com.varivoda.igor.tvz.financijskimanager.util.rawSerial
import com.varivoda.igor.tvz.financijskimanager.util.toSha2
import java.lang.Exception

class LoginRepository(private val preferences: Preferences,
                      private val connectivityAgent: ConnectivityAgent,
                        private val database: AppDatabase) : BaseLoginRepository{

    val api = Api.retrofitService

    override fun login(username: String, password: String): NetworkResult<Boolean> {
        if(connectivityAgent.isDeviceConnectedToInternet) {
            return try {
                val response = api.validateUsernameAndPassword(LoginEntry(username, password.toSha2())).execute()
                when(response.code()){
                    200 -> {
                        preferences.insertUserToken(response.body()!!.token)
                        NetworkResult.Success(true)
                    }
                    401 -> {
                        NetworkResult.Error(Exception("401"))
                    }
                    else -> NetworkResult.Error(Exception(response.message()))
                }
            }catch (ex: Exception){
                NetworkResult.Error(ex)
            }

        }else{
            return NetworkResult.NoNetworkConnection("")
        }
    }

    override fun loginByFingerprint(username: String): NetworkResult<Boolean> {
        if(connectivityAgent.isDeviceConnectedToInternet){
            return try {
                val response = api.loginByFingerprint(FingerprintEntry(username,username+ preferences.getSerialNumber().toSha2())).execute()
                when(response.code()){
                    200 -> {
                        preferences.insertUserToken(response.body()!!.token)
                        NetworkResult.Success(true)
                    }
                    401 -> {
                        NetworkResult.Error(Exception("401"))
                    }
                    else -> NetworkResult.Error(Exception(response.message()))
                }
            }catch (ex: Exception){
                NetworkResult.Error(ex)
            }
        }else{
            return NetworkResult.NoNetworkConnection("")
        }
    }

    override fun addFingerprint(fingerprintEntry: FingerprintEntry): NetworkResult<Boolean> {
        if(connectivityAgent.isDeviceConnectedToInternet){
            return try {
                val response = api.addFingerPrintToUser(preferences.getUserToken()!!, fingerprintEntry).execute()
                println("debug response je ${response.code()}  ${response.message()}")
                when(response.code()){
                    200 -> {
                        //preferences.insertUserToken(response.body()!!.token)
                        NetworkResult.Success(true)
                    }
                    401 -> {
                        NetworkResult.Error(Exception("401"))
                    }
                    else -> NetworkResult.Error(Exception(response.message()))
                }
            }catch (ex: Exception){
                NetworkResult.Error(ex)
            }
        }else{
            return NetworkResult.NoNetworkConnection("")
        }
    }

    override fun getAllData() : NetworkResult<Boolean>{
        if(connectivityAgent.isDeviceConnectedToInternet){
            return try {
                val response = api.getAllData(preferences.getUserToken()!!).execute()
                println("debug response je ${response.code()}  ${response.message()}")
                when(response.code()){
                    200 -> {
                        insertData(response.body()!!)
                        NetworkResult.Success(true)
                    }
                    401 -> {
                        NetworkResult.Error(Exception("401"))
                    }
                    else -> NetworkResult.Error(Exception(response.message()))
                }
            }catch (ex: Exception){
                NetworkResult.Error(ex)
            }
        }else{
            return NetworkResult.NoNetworkConnection("")
        }
    }

    private fun insertData(body: AllData) {
        database.countyDao.insertAllCounties(body.counties)
        database.locationDao.insertAllLocations(body.locations)
        database.storesDao.insertAllStores(body.stores)
        database.customerDao.insertAllCustomers(body.customers)
        database.employeeDao.insertAllEmployees(body.employees)
        database.categoryDao.insertAllCategories(body.categories)
        database.productDao.insertAllProducts(body.products)
        database.billDao.insertAllPaymentMethods(body.paymentMethods)
        database.stockDao.insertAllStocks(body.stockData)
        database.productDao.insertAllInventories(body.inventoryItems)
        database.billDao.insertAllBills(body.bills)
        database.productOnBillDao.insertAllProductsOnBill(body.productsOnBills)
    }

}