package com.varivoda.igor.tvz.financijskimanager.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.LoginRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseLoginRepository

class LoginViewModelFactory(private val preferences: Preferences, private val loginRepository: BaseLoginRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(preferences,loginRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}