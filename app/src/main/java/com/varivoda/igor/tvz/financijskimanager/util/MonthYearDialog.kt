package com.varivoda.igor.tvz.financijskimanager.util

import android.app.Activity
import com.varivoda.igor.tvz.financijskimanager.R
import com.whiteelephant.monthpicker.MonthPickerDialog
import java.util.*

class MonthYearDialog {


    fun getDialog(activity: Activity,
                    changeDate: (Int,Int) -> Unit) {
        val today = Calendar.getInstance()
        val builder: MonthPickerDialog.Builder =
            MonthPickerDialog.Builder(activity, { selectedMonth, selectedYear ->
                changeDate.invoke(selectedMonth+1,selectedYear)
            }, today[Calendar.YEAR], today[Calendar.MONTH])
        builder.setActivatedMonth(today.get(Calendar.MONTH))
            .setMinYear(1990)
            .setActivatedYear(today.get(Calendar.YEAR))
            .setMaxYear(2030)
            .setMinMonth(Calendar.FEBRUARY)
            .setTitle(activity.getString(R.string.choose_month_year))
            .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)
            .build()
            .show()
    }
}