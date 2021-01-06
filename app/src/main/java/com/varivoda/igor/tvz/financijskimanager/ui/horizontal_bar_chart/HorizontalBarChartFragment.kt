package com.varivoda.igor.tvz.financijskimanager.ui.horizontal_bar_chart

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.varivoda.igor.tvz.financijskimanager.App
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.util.MonthYearDialog
import com.varivoda.igor.tvz.financijskimanager.util.getMonthAndYearFormatted
import com.varivoda.igor.tvz.financijskimanager.util.getMonthWithZero
import kotlinx.android.synthetic.main.fragment_horizontal_bar_chart.*
import kotlinx.android.synthetic.main.fragment_horizontal_bar_chart.changePeriod
import kotlinx.android.synthetic.main.fragment_horizontal_bar_chart.timePeriod


class HorizontalBarChartFragment : Fragment() {

    private val viewModel by viewModels<HorizontalBarChartViewModel> {
        HorizontalBarChartViewModelFactory((requireContext().applicationContext as App).employeeRepository,
            (requireContext().applicationContext as App).storeRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_horizontal_bar_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //loadChart()
        (activity as HomeActivity).setActionBarText("Zaposlenici - statistika")
        timePeriod.text = getString(R.string.time_period, getMonthAndYearFormatted())
        observeBarChartStatistics()

        changePeriod.setOnClickListener {
            if(useMonthAndYearSwitch.isChecked) {
                MonthYearDialog().getDialog(activity as HomeActivity, changeDate)
            }else{
                MonthYearDialog().getOnlyYearDialog(activity as HomeActivity, changeYear)
            }
        }
        selectStore.setOnClickListener { storeDialog() }
        observeStoreSelected()
    }

    private fun observeStoreSelected() {
        viewModel.currentStore.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            selectStore.setText(it.storeName)
        })
    }

    private fun observeBarChartStatistics() {
        viewModel.barChartStatistics.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            loadChart()
        })
    }

    private val changeDate: (month: Int, year: Int) -> Unit = {
            month, year ->
        viewModel.monthAndYear.value = Pair(getMonthWithZero(month),year.toString())
        timePeriod.text = getString(R.string.time_period,getMonthAndYearFormatted(month, year))
    }

    private val changeYear: (year: Int) -> Unit = {
            year ->
        viewModel.monthAndYear.value = Pair("-1",year.toString())
        timePeriod.text = getString(R.string.time_period,year.toString())
    }

    private var storeDialog: AlertDialog? = null
    private fun storeDialog() {
        viewModel.allStores.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.select_store))
            builder.setItems(it.map { itt -> itt.storeName }.toTypedArray()
            ) { dialog, which ->
                viewModel.currentStore.value = it[which]
                dialog?.dismiss() }
            storeDialog?.cancel()
            storeDialog = builder.create()
            storeDialog?.show()
        })

    }

    private fun loadChart() {
        val firstEntries = mutableListOf<BarEntry>()
        var i = 1
        viewModel.barChartStatistics.value?.forEach {
            firstEntries.add(BarEntry(i.toFloat(),it.totalInvoice))
            i += 1
        }

        val barDataSet = BarDataSet(firstEntries,"")
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS.toList())
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 16f
        val barData = BarData(barDataSet)
        horizontalBarChart.setFitBars(true)
        horizontalBarChart.xAxis.labelCount = viewModel.barChartStatistics.value?.size ?: 0
        val names = mutableListOf<String>()
        names.add("")
        viewModel.barChartStatistics.value?.map { it.name }?.let { names.addAll(it) }
        horizontalBarChart.xAxis.valueFormatter = IndexAxisValueFormatter(names)
        horizontalBarChart.data = barData
        horizontalBarChart.description.text = ""
        horizontalBarChart.animateY(2000)
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