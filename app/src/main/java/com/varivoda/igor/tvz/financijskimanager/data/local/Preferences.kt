package com.varivoda.igor.tvz.financijskimanager.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.varivoda.igor.tvz.financijskimanager.util.Storable
import java.lang.Exception
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType


class Preferences(appContext: Context) {

    private val sharedPreferences: SharedPreferences =
        appContext.getSharedPreferences("appPreferences", Context.MODE_PRIVATE)

    private val appPreferences = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun getCachedUsername(): String = sharedPreferences.getString("username key","") ?: ""

    fun getCachedPassword(): String = sharedPreferences.getString("password key","") ?: ""

    fun setCachedUsername(username: String){
        sharedPreferences.edit().putString("username key",username).apply()
    }

    fun setCachedPassword(username: String){
        sharedPreferences.edit().putString("password key",username).apply()
    }

    fun setRememberMe(b: Boolean){
        sharedPreferences.edit().putBoolean("remember me",b).apply()
    }

    fun getCachedRememberMeOption(): Boolean = sharedPreferences.getBoolean("remember me",false)

    fun getVibrationsOption(): Boolean = appPreferences.getBoolean("vibrations key",true)

    fun getNotificationsOption(): Boolean = appPreferences.getBoolean("notifications key",true)

    fun getToastMessageDesign(): String? = appPreferences.getString("toast key","default") ?: "default"

    fun setSeekBarValue(float: Float){
        val editor = sharedPreferences.edit()
        editor.putFloat("brightness key",float).apply()
    }

    fun getSeekBarValue(): Float = sharedPreferences.getFloat("brightness key",0.5f)

    fun clear(value: String) = sharedPreferences.edit().remove(value).apply()

    fun insertUserToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString("user token",token).apply()
    }

    fun getUserToken(): String? = sharedPreferences.getString("user token",null)

    fun saveStorableObject(storable: Storable){
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter: JsonAdapter<Storable> = moshi.adapter(Storable::class.java)
        val serialized = jsonAdapter.toJson(storable)
        val editor = sharedPreferences.edit()
        editor.putString("storable",serialized).apply()
    }

    fun getStorable(): Storable?{

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter: JsonAdapter<Storable> = moshi.adapter(Storable::class.java)
        val serialized = sharedPreferences.getString("storable", null)
        if (serialized.isNullOrBlank()) {
            return null
        }
        return try {
            jsonAdapter.fromJson(serialized)
        }catch (ex: Exception){
            return null
        }
    }
}