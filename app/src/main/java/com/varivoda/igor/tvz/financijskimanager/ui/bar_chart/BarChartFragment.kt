package com.varivoda.igor.tvz.financijskimanager.ui.bar_chart

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.varivoda.igor.tvz.financijskimanager.App
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.util.MonthYearDialog
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentYear
import kotlinx.android.synthetic.main.fragment_bar_chart.*
import kotlinx.android.synthetic.main.fragment_bar_chart.changePeriod
import kotlinx.android.synthetic.main.fragment_bar_chart.timePeriod


class BarChartFragment : Fragment() {

    private val barChartViewModel by viewModels<BarChartViewModel> {
        BarChartViewModelFactory((requireContext().applicationContext as App).productRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bar_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reloadGraph()
        timePeriod.text = getString(R.string.time_period, getCurrentYear())
        changePeriod.setOnClickListener {
            MonthYearDialog().getOnlyYearDialog(activity as HomeActivity,changeYear)
        }
        observeBarChartStatistics()
    }

    private fun observeBarChartStatistics() {
        barChartViewModel.barChartStatistics.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            reloadGraph()
        })
    }

    private val changeYear: (year: Int) -> Unit = {
            year ->
        barChartViewModel.yearSelected.value = year.toString()
        timePeriod.text = getString(R.string.time_period,year.toString())
    }


    private fun reloadGraph() {
        val firstEntries = mutableListOf<BarEntry>()
        barChartViewModel.barChartStatistics.value?.forEach {
            firstEntries.add(BarEntry(it.month.toFloat(),it.total.toFloat()))
        }

        val barDataSet = BarDataSet(firstEntries,getString(R.string.months_label))
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS.toList())
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 16f
        val barData = BarData(barDataSet)
        barChart.setFitBars(true)
        barChart.data = barData
        barChart.description.text = ""
        barChart.animateY(2000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }
}