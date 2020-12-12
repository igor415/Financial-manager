package com.varivoda.igor.tvz.financijskimanager.data.local.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.varivoda.igor.tvz.financijskimanager.data.local.remote.model.LoginEntry
import com.varivoda.igor.tvz.financijskimanager.data.local.remote.model.LoginResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

private const val BASE_URL = "http://192.168.0.169:8080"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ApiService {

    @POST("login/authenticate")
    fun validateUsernameAndPassword(@Body loginEntry: LoginEntry): Call<LoginResponse>
}

object Api {
    val retrofitService : ApiService by lazy {
        retrofit.create(ApiService::class.java) }
}