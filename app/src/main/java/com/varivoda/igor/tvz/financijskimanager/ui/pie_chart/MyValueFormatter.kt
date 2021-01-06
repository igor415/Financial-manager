package com.varivoda.igor.tvz.financijskimanager.ui.pie_chart

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import java.text.DecimalFormat


class MyValueFormatter : ValueFormatter() {

    private val mFormatt: DecimalFormat = DecimalFormat("###,###,##0.0")

    override fun getFormattedValue(value: Float): String {
        return mFormatt.format(value) + "%"
    }
    override fun getFormattedValue(
        value: Float,
        entry: Entry?,
        dataSetIndex: Int,
        viewPortHandler: ViewPortHandler?
    ): String {
        return mFormatt.format(value) + "%"
    }


}