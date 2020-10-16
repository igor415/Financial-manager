package com.varivoda.igor.tvz.financijskimanager.util

import android.content.Context
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences

fun showSelectedToast(context: Context, text: String){

    val pref = Preferences(context)
    when(pref.getToastMessageDesign()){
        "default" -> context.toast(text)
        "dark" -> context.styleableToast(text,R.style.darkToast)
        "light" -> context.styleableToast(text,R.style.lightToast)
    }
}