package com.varivoda.igor.tvz.financijskimanager.ui.horizontal_bar_chart

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class XAxisValueFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {

        return "222"
    }


    override fun getAxisLabel(value: Float, axis: AxisBase): String {

      return "xxxx"
    }

//    fun getXValue(
//        dateInMillisecons: String,
//        index: Int,
//        viewPortHandler: ViewPortHandler?
//    ): String {
//        return try {
//            val dateFormatter =
//                DateTimeFormatter.ofPattern("dd.MM.yyyy")
//            val date =
//                Instant.ofEpochMilli(dateInMillisecons.toLong())
//                    .atZone(ZoneId.systemDefault()).toLocalDate()
//            dateFormatter.format(date)
//        } catch (e: Exception) {
//            dateInMillisecons
//        }
//    }
}