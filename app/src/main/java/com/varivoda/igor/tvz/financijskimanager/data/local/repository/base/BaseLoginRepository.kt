package com.varivoda.igor.tvz.financijskimanager.data.local.repository.base

import com.varivoda.igor.tvz.financijskimanager.data.local.remote.model.FingerprintEntry
import com.varivoda.igor.tvz.financijskimanager.util.NetworkResult

interface BaseLoginRepository {
    fun login(username: String, password: String): NetworkResult<Boolean>
    fun loginByFingerprint(username: String): NetworkResult<Boolean>
    fun addFingerprint(fingerprintEntry: FingerprintEntry): NetworkResult<Boolean>
}