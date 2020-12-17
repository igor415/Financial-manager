package com.varivoda.igor.tvz.financijskimanager.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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


    fun evaluateCredentials(){
        Timber.d("username : $currentUsername password $currentPassword")
        doAnimation.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            delay(1300)
            loginSuccess.postValue(loginRepository.login(currentUsername,currentPassword))
            //loginSuccess.postValue(NetworkResult.Success(true))
        }

    }
}