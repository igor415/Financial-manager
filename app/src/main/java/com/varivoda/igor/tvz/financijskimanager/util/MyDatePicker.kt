package com.varivoda.igor.tvz.financijskimanager.util

import android.R
import android.app.DatePickerDialog
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import java.util.*

class MyDatePicker {

    fun openDialog(activity: HomeActivity,
                   dateListener: DatePickerDialog.OnDateSetListener){
        val currentTime = System.currentTimeMillis()
        val mCalendar: Calendar = Calendar.getInstance()
        val year: Int = mCalendar.get(Calendar.YEAR)
        val month: Int = mCalendar.get(Calendar.MONTH)
        val day: Int = mCalendar.get(Calendar.DAY_OF_MONTH)

        val mDatePickerDialog =
            DatePickerDialog(
                activity,
                R.style.Theme_Material_Dialog_NoActionBar,
                dateListener,
                year, month, day
            )
        mDatePickerDialog.show()
    }
}