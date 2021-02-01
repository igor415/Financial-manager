package com.varivoda.igor.tvz.financijskimanager.data.local.remote

import com.google.gson.GsonBuilder
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Employee
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.InventoryItem
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.data.local.remote.model.AllData
import com.varivoda.igor.tvz.financijskimanager.data.local.remote.model.FingerprintEntry
import com.varivoda.igor.tvz.financijskimanager.data.local.remote.model.LoginEntry
import com.varivoda.igor.tvz.financijskimanager.data.local.remote.model.LoginResponse
import com.varivoda.igor.tvz.financijskimanager.model.InventoryDTO
import com.varivoda.igor.tvz.financijskimanager.model.ReturnData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

private const val BASE_URL = "http://192.168.0.169:8080"

/*private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()*/

private val gson = GsonBuilder()
    .setDateFormat("yyyy-MM-dd HH:mm:ss.sss")
    .setLenient()
    .create()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .baseUrl(BASE_URL)
    .build()

interface ApiService {

    @POST("login/authenticate")
    fun validateUsernameAndPassword(@Body loginEntry: LoginEntry): Call<LoginResponse>

    @POST("login/fingerprint")
    fun addFingerPrintToUser(@Header("Authorization") token: String, @Body fingerprintEntry: FingerprintEntry): Call<ResponseBody>

    @POST("login/fingerprintauthentication")
    fun loginByFingerprint(@Body fingerprintEntry: FingerprintEntry): Call<LoginResponse>

    @GET("main")
    fun getAllData(@Header("Authorization") token: String): Call<AllData>

    @PUT("main/image")
    fun sendImageString(@Header("Authorization") token: String, @Body product: Product): Call<ResponseBody>

    @POST("main/employee")
    fun changeEmployeeInfo(@Header("Authorization") token: String, @Body employee: Employee): Call<ResponseBody>

    @POST("main/product")
    fun addOrEditProduct(@Header("Authorization") token: String, @Body product: Product): Call<ResponseBody>

    @POST("main/return")
    fun returnItems(@Header("Authorization") token: String, @Body returnData: List<ReturnData>): Call<ResponseBody>

    @POST("main/inventory")
    fun executeInventory(@Header("Authorization") token: String, @Body list: List<InventoryDTO>): Call<ResponseBody>

    @POST("main/add-inventory-item")
    fun addInventoryItem(@Header("Authorization") token: String, @Body inventoryItem: InventoryItem): Call<ResponseBody>
}

object Api {
    val retrofitService : ApiService by lazy {
        retrofit.create(ApiService::class.java) }
}