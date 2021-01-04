package com.varivoda.igor.tvz.financijskimanager.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseLoginRepository
import java.lang.IllegalArgumentException

class SettingsViewModelFactory (private val loginRepository: BaseLoginRepository, private val preferences: Preferences): ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SettingsViewModel::class.java)){
            return SettingsViewModel(loginRepository, preferences) as T
        }
        throw IllegalArgumentException("wrong argument")
    }

}