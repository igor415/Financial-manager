package com.varivoda.igor.tvz.financijskimanager.ui.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.App
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseLoginRepository
import com.varivoda.igor.tvz.financijskimanager.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginViewModel(preferences: Preferences,
                     private val loginRepository: BaseLoginRepository): ViewModel() {

    var loginSuccess = MutableLiveData<NetworkResult<Boolean>>()
    var currentUsername: String = preferences.getCachedUsername()
    var currentPassword: String = preferences.getCachedPassword()
    var rememberMe: Boolean = preferences.getCachedRememberMeOption()
    var doAnimation = MutableLiveData<Boolean>()
    var context: Context? = null


    fun evaluateCredentials(){
        Timber.d("username : $currentUsername password $currentPassword")
        doAnimation.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            delay(1300)
            //val result = loginRepository.login(currentUsername,currentPassword)
            //loginSuccess.postValue(result)
            val result = NetworkResult.Success(true)
            loginSuccess.postValue(result)
            context?.let { context ->
                if(result is NetworkResult.Success){
                    (context as App).setDatabase(currentPassword)
                }
            }
        }

    }
}