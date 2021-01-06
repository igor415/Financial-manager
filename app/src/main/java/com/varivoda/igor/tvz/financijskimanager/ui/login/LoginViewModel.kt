package com.varivoda.igor.tvz.financijskimanager.ui.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.App
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseLoginRepository
import com.varivoda.igor.tvz.financijskimanager.util.NetworkResult
import com.varivoda.igor.tvz.financijskimanager.util.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class LoginViewModel(preferences: Preferences,
                     private val loginRepository: BaseLoginRepository, private val appContext: Context): ViewModel() {

    var loginSuccess = MutableLiveData<NetworkResult<Boolean>>()
    var currentUsername: String = preferences.getCachedUsername()
    var currentPassword: String = preferences.getCachedPassword()
    var rememberMe: Boolean = preferences.getCachedRememberMeOption()
    var doAnimation = MutableLiveData<Boolean>()


    fun evaluateCredentials(){
        Timber.d("username : $currentUsername password $currentPassword")
        if(currentUsername.isNotEmpty() && currentPassword.isNotEmpty()) {
            doAnimation.postValue(true)
            viewModelScope.launch(Dispatchers.IO) {
                delay(1300)
                //val result = loginRepository.login(currentUsername,currentPassword)
                //loginSuccess.postValue(result)
                val result = NetworkResult.Success(true)
                appContext?.let { context ->
                    if (result is NetworkResult.Success) {
                        if (currentPassword.isNotEmpty()) {
                            val bol = (context as App).setDatabase(currentPassword)
                            if (bol) {
                                loginSuccess.postValue(result)
                            } else {
                                loginSuccess.postValue(NetworkResult.Success(false))
                            }

                        }

                    }
                }
            }

        }else{
            appContext.toast("You need to enter username and password")
        }
    }

    fun fingerPrintAuthenticationSuccess(){
        currentPassword = "test"
        if(currentUsername.isNotEmpty()){
            viewModelScope.launch(Dispatchers.IO) {
                val result = loginRepository.loginByFingerprint(currentUsername)
                if (result is NetworkResult.Success) {
                    appContext?.let { context ->
                        val bol = (context as App).setDatabase("test")
                        if (bol) {
                            loginSuccess.postValue(result)
                        } else {
                            loginSuccess.postValue(NetworkResult.Success(false))
                        }
                    }
                }else{
                    loginSuccess.postValue(NetworkResult.Error(Exception("")))
                }

            }
        }else{
            appContext.toast("You need to enter username for fingerprint authentication")
        }


    }
}