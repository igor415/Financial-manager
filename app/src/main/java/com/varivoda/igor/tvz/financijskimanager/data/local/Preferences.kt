package com.varivoda.igor.tvz.financijskimanager.data.local

import android.content.Context
import android.content.SharedPreferences

class Preferences(appContext: Context) {

    private val sharedPreferences: SharedPreferences =
        appContext.getSharedPreferences("appPreferences", Context.MODE_PRIVATE)

    fun getCachedUsername(): String = sharedPreferences.getString("username key","") ?: ""

    fun getCachedPassword(): String = sharedPreferences.getString("password key","") ?: ""

    fun getCachedRememberMeOption(): Boolean = sharedPreferences.getBoolean("remember me",false)

}