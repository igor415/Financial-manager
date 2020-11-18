package com.varivoda.igor.tvz.financijskimanager.util

import android.content.Context
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import java.util.*

fun showSelectedToast(context: Context, text: String){

    val pref = Preferences(context)
    when(pref.getToastMessageDesign()){
        "default" -> context.toast(text)
        "dark" -> context.styleableToast(text,R.style.darkToast)
        "light" -> context.styleableToast(text,R.style.lightToast)
    }
}

fun getMonthAndYearFormatted(month: Int? = null, year: Int? = null): String{
    val cal = Calendar.getInstance()
    if(month != null && year != null){
        return if(month >= 10){
            "$month.$year."
        }else{
            val monthFormatted = "0$month"
            "$monthFormatted.$year."
        }
    }else{
        val monthValue: Int = cal.get(Calendar.MONTH) + 1
        val yearValue: Int = cal.get(Calendar.YEAR)

        return if(monthValue >= 10){
            "$monthValue.$yearValue."
        }else{
            val monthFormatted = "0$monthValue"
            "$monthFormatted.$yearValue."
        }
    }

}

fun getCurrentYear(): String{
    val cal = Calendar.getInstance()
    return cal.get(Calendar.YEAR).toString()
}

fun getCurrentMonth(): String{
    val cal = Calendar.getInstance()
    return (cal.get(Calendar.MONTH)+1).toString()
}

fun getMonthWithZero(month: Int): String{
    return if(month >= 10){
        month.toString()
    }else{
        "0$month"
    }
}