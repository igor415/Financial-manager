package com.varivoda.igor.tvz.financijskimanager.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class Preferences(appContext: Context) {

    private val sharedPreferences: SharedPreferences =
        appContext.getSharedPreferences("appPreferences", Context.MODE_PRIVATE)

    private val appPreferences = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun getCachedUsername(): String = sharedPreferences.getString("username key","") ?: ""

    fun getCachedPassword(): String = sharedPreferences.getString("password key","") ?: ""

    fun getCachedRememberMeOption(): Boolean = sharedPreferences.getBoolean("remember me",false)

    fun getVibrationsOption(): Boolean = appPreferences.getBoolean("vibrations key",true)

    fun getNotificationsOption(): Boolean = appPreferences.getBoolean("notifications key",true)

    fun getToastMessageDesign(): String? = appPreferences.getString("toast key","default") ?: "default"

}