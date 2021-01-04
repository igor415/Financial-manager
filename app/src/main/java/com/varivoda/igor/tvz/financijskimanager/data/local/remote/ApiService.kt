package com.varivoda.igor.tvz.financijskimanager.data.local.remote

import com.google.gson.GsonBuilder
import com.varivoda.igor.tvz.financijskimanager.data.local.remote.model.FingerprintEntry
import com.varivoda.igor.tvz.financijskimanager.data.local.remote.model.LoginEntry
import com.varivoda.igor.tvz.financijskimanager.data.local.remote.model.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

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
}

object Api {
    val retrofitService : ApiService by lazy {
        retrofit.create(ApiService::class.java) }
}