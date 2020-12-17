package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.data.local.remote.Api
import com.varivoda.igor.tvz.financijskimanager.data.local.remote.model.LoginEntry
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseLoginRepository
import com.varivoda.igor.tvz.financijskimanager.monitoring.ConnectivityAgent
import com.varivoda.igor.tvz.financijskimanager.util.NetworkResult
import com.varivoda.igor.tvz.financijskimanager.util.toSha2
import java.lang.Exception

class LoginRepository(private val preferences: Preferences,
                      private val connectivityAgent: ConnectivityAgent) : BaseLoginRepository{

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

}