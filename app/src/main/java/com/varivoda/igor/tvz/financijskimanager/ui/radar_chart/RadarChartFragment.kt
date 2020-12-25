package com.varivoda.igor.tvz.financijskimanager.ui.radar_chart

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.gms.maps.model.Marker
import com.varivoda.igor.tvz.financijskimanager.App
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.util.CustomPeriod
import com.varivoda.igor.tvz.financijskimanager.util.MonthYearDialog
import kotlinx.android.synthetic.main.fragment_radar_chart.*


class RadarChartFragment : Fragment() {

    private val viewModel by viewModels<RadarChartViewModel> {
        RadarChartViewModelFactory((requireContext().applicationContext as App).storeRepository)
    }
    private var periodDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_radar_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectDiscountPeriod.setOnClickListener {
            discountDialog()
        }
        changePeriod.setOnClickListener {
            MonthYearDialog().getOnlyYearDialog(activity as HomeActivity,changeYear)
        }
        observeRadarChartData()
        observeYear()
    }

    private val changeYear: (year: Int) -> Unit = {
            year ->
        viewModel.year.value = year.toString()
    }


    private fun observeYear() {
        viewModel.year.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            timePeriod.text = getString(R.string.time_period,it)
        })
    }

    private fun observeRadarChartData() {
        viewModel.radarChartData.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            loadChart()
        })
    }

    private fun loadChart() {
        val radarEntries = mutableListOf<RadarEntry>()
        viewModel.radarChartData.value?.forEach {
            radarEntries.add(RadarEntry(it.number.toFloat()))
        }
        val names = viewModel.radarChartData.value?.map { it.storeName }?.toTypedArray() ?: arrayOf()
        val dataSet = RadarDataSet(radarEntries, "")
        dataSet.valueTextSize = 0f
        dataSet.setColor(Color.GREEN)
        dataSet.fillColor = Color.GREEN
        dataSet.setDrawFilled(true)
        val data = RadarData(dataSet)
        myRadarChart.data = data
        myRadarChart.description.isEnabled = false
        myRadarChart.webLineWidth = 1.5f
        myRadarChart.webColor = Color.GRAY
        myRadarChart.webLineWidthInner = 1.5f
        myRadarChart.webColorInner = Color.GRAY
        myRadarChart.animateXY(1400, 1400, Easing.EaseInOutQuad)
        myRadarChart.getXAxis().setValueFormatter(IndexAxisValueFormatter(names))
        myRadarChart.invalidate()
    }

    private fun discountDialog() {
        val periodArray = CustomPeriod.values().map { it.fullName }.toTypedArray()
        CustomPeriod.values().map { it.fullName }
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.select_discount_period))
            builder.setItems(periodArray
            ) { dialog, which ->
                viewModel.enum.value = periodArray[which]
                selectDiscountPeriod.setText(periodArray[which])
                dialog?.dismiss() }
            periodDialog?.cancel()
            periodDialog = builder.create()
            periodDialog?.show()


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