package com.varivoda.igor.tvz.financijskimanager.ui.bar_chart

import android.app.AlertDialog
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.varivoda.igor.tvz.financijskimanager.App
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.util.MonthYearDialog
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentYear
import kotlinx.android.synthetic.main.employee_item.*
import kotlinx.android.synthetic.main.fragment_bar_chart.*


class BarChartFragment : Fragment() {

    private val barChartViewModel by viewModels<BarChartViewModel> {
        BarChartViewModelFactory(
            (requireContext().applicationContext as App).productRepository,
            (requireContext().applicationContext as App).storeRepository
        )
    }
    private var storeDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bar_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timePeriod.text = getString(R.string.time_period, getCurrentYear())
        changePeriod.setOnClickListener {
            MonthYearDialog().getOnlyYearDialog(activity as HomeActivity, changeYear)
        }
        observeBarChartStatistics()
        selectStore.setOnClickListener {
            storeDialog()
        }
    }

    private fun observeBarChartStatistics() {
        barChartViewModel.barChartStatistics.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            reloadGraph()
        })
    }

    private val changeYear: (year: Int) -> Unit = { year ->
        barChartViewModel.yearSelected.value = year.toString()
        timePeriod.text = getString(R.string.time_period, year.toString())
    }


    private fun reloadGraph() {
        val firstEntries = mutableListOf<BarEntry>()
        val secondEntries = mutableListOf<BarEntry>()
        barChartViewModel.barChartStatistics.value?.first?.forEach {
            firstEntries.add(BarEntry(it.month.toFloat(), it.total.toFloat()))
        }
        barChartViewModel.barChartStatistics.value?.second?.forEach {
            secondEntries.add(BarEntry(it.month.toFloat(), it.total.toFloat()))
        }
        val barDataSet = BarDataSet(firstEntries,barChartViewModel.currentStore.value!!.first.storeName)
        barDataSet.setColor(Color.YELLOW)
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 16f

        val barDataSet2 = BarDataSet(secondEntries,barChartViewModel.currentStore.value!!.second.storeName)
        barDataSet.setColor(Color.GREEN)
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 16f

        val barData = BarData(barDataSet,barDataSet2)
        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(arrayOf("I","II","III","IV"))
        xAxis.setCenterAxisLabels(true)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.spaceMin = 2.9f

        barChart.data = barData

        val barSpace = 0.1f
        val groupSpace = 0.33f
        barData.barWidth = 0.25f
        xAxis.axisMinimum = 0f
        barChart.groupBars(0f, groupSpace, barSpace)

        barChart.description.text = ""
        barChart.animateY(2000)
    }

    private fun storeDialog() {
        barChartViewModel.allStores.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.select_two_stores))
            val listOfSelectedItems = mutableListOf<Store>()
            val arrayStores = it.map { itt -> itt.storeName }.toTypedArray()
            builder.setMultiChoiceItems(
                arrayStores, null
            ) { dialog, which, isChecked ->
                if (isChecked) {
                    listOfSelectedItems.add(it[which])
                } else {
                    if (it[which] in listOfSelectedItems) {
                        listOfSelectedItems.remove(it[which])
                    }
                }
                storeDialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled =
                    listOfSelectedItems.size == 2
            }
            builder.setPositiveButton(
                getString(R.string.confirm)
            ) { dialog, which ->

                var result = ""
                listOfSelectedItems.forEach {
                    result += "${it.storeName!!},"
                }
                barChartViewModel.currentStore.value =
                    Pair(listOfSelectedItems[0], listOfSelectedItems[1])
                selectStore.setText(result.substringBeforeLast(","))
                dialog?.dismiss()

            }
            storeDialog?.cancel()
            storeDialog = builder.create()
            storeDialog?.show()
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onPause() {
        super.onPause()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }


}