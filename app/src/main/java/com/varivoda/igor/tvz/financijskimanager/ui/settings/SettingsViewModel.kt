package com.varivoda.igor.tvz.financijskimanager.ui.settings

import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.data.local.remote.model.FingerprintEntry
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseLoginRepository
import com.varivoda.igor.tvz.financijskimanager.util.NetworkResult
import com.varivoda.igor.tvz.financijskimanager.util.rawSerial
import com.varivoda.igor.tvz.financijskimanager.util.toSha2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class SettingsViewModel (private val loginRepository: BaseLoginRepository, private val preferences: Preferences): ViewModel(){

    val result = MutableLiveData<NetworkResult<Boolean>>()

    fun addFingerprint() {
        viewModelScope.launch(Dispatchers.IO) {
            result.postValue(loginRepository.addFingerprint(FingerprintEntry(preferences.getCachedUsername(),preferences.getCachedUsername()+preferences.getSerialNumber().toSha2())))
        }
    }


}