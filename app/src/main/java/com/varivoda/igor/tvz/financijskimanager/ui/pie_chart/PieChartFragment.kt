package com.varivoda.igor.tvz.financijskimanager.ui.pie_chart

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.varivoda.igor.tvz.financijskimanager.App
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.util.MonthYearDialog
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentYear
import kotlinx.android.synthetic.main.fragment_bar_chart.*
import kotlinx.android.synthetic.main.fragment_pie_chart.*
import kotlinx.android.synthetic.main.fragment_pie_chart.changePeriod
import kotlinx.android.synthetic.main.fragment_pie_chart.timePeriod

class PieChartFragment : Fragment() {

    private val pieChartViewModel by viewModels<PieChartViewModel> {
        PieChartViewModelFactory((requireContext().applicationContext as App).storeRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pie_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadChart()
        changePeriod.setOnClickListener {
            MonthYearDialog().getOnlyYearDialog(activity as HomeActivity,changeYear)
        }
        observePieChartStatistics()
        observeSelectedYear()
    }

    private fun observeSelectedYear() {
        pieChartViewModel.yearSelected.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            timePeriod.text = getString(R.string.time_period,it)
        })
    }

    private fun observePieChartStatistics() {
        pieChartViewModel.pieChartStatistics.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            loadChart()
        })
    }

    private val changeYear: (year: Int) -> Unit = {
            year ->
        pieChartViewModel.yearSelected.value = year.toString()
    }

    private fun loadChart() {
        val pieEntries = mutableListOf<PieEntry>()
        var smallestCounter: Float = 0.0f
        pieChartViewModel.pieChartStatistics.value?.forEach {
            if(it.total.toFloat() < 8.0f){
                smallestCounter += it.total.toFloat()
            }else{
                pieEntries.add(PieEntry(it.total.toFloat(),it.storeName))
            }
        }
        pieEntries.add(PieEntry(smallestCounter,getString(R.string.other_stores_label)))

        val dataSet = PieDataSet(pieEntries,"")
        dataSet.setColors(mutableListOf(Color.parseColor("#e3b022"),Color.parseColor("#c42116")
            ,Color.parseColor("#8d67cf"),Color.parseColor("#6b4227")
            ,Color.parseColor("#0f83d6"),Color.parseColor("#2ca80a")))
        dataSet.valueTextSize = 18f
        dataSet.valueTextColor = Color.WHITE
        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.description.text = ""
        pieChart.animateY(1500)
        pieChart.setCenterTextSize(18f)

        pieChart.invalidate()
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