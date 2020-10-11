package com.varivoda.igor.tvz.financijskimanager.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.ProductRepository
import com.varivoda.igor.tvz.financijskimanager.util.Result
import timber.log.Timber

class LoginViewModel(preferences: Preferences): ViewModel() {

    var loginSuccess = MutableLiveData<Result<String>>()
    var currentUsername: String = preferences.getCachedUsername()
    var currentPassword: String = preferences.getCachedPassword()
    var rememberMe: Boolean = preferences.getCachedRememberMeOption()


    fun evaluateCredentials(){
        Timber.d("username : $currentUsername password $currentPassword")
        loginSuccess.postValue(Result.Success(""))
    }
}