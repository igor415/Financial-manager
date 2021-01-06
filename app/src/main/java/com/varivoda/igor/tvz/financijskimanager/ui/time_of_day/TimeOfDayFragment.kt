package com.varivoda.igor.tvz.financijskimanager.ui.time_of_day

import android.app.AlertDialog
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
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.util.MonthYearDialog
import com.varivoda.igor.tvz.financijskimanager.util.getMonthWithZero
import kotlinx.android.synthetic.main.fragment_time_of_day.*
import kotlinx.android.synthetic.main.fragment_time_of_day.changePeriod
import kotlinx.android.synthetic.main.fragment_time_of_day.timePeriod


class TimeOfDayFragment : Fragment() {

    private val viewModel by viewModels<TimeOfDayViewModel> {
        TimeOfDayViewModelFactory((requireContext().applicationContext as App).storeRepository)
    }
    private var storeDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_of_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changePeriod.setOnClickListener {
                MonthYearDialog().getDialog(activity as HomeActivity, changeDate)
        }
        selectStore.setOnClickListener { storeDialog() }
        observeStoreSelected()
        observeDateSelected()
        observeTimeOfDayResult()
    }

    private fun observeTimeOfDayResult() {
        viewModel.timeOfDayData.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            println("debug it je $it")
            loadChart()
        })
    }

    private fun loadChart() {
        val firstEntries = mutableListOf<BarEntry>()
        var i = 1
        viewModel.timeOfDayData.value?.forEach {
            firstEntries.add(BarEntry(i.toFloat(),it.number.toFloat()))
            i += 1
        }

        val barDataSet = BarDataSet(firstEntries.reversed(),"Attendance")
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS.toList())
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 0f
        val barData = BarData(barDataSet)
        timeOfDayChart.setFitBars(true)
        timeOfDayChart.xAxis.labelCount = viewModel.timeOfDayData.value?.size ?: 0
        val names = mutableListOf<String>()
        names.add("")
        viewModel.timeOfDayData.value?.map { it.time }?.let { names.addAll(it) }
        timeOfDayChart.xAxis.valueFormatter = IndexAxisValueFormatter(names)
        timeOfDayChart.data = barData
        timeOfDayChart.description.text = ""
        timeOfDayChart.setExtraOffsets(10f,10f,10f,15f)
        timeOfDayChart.animateY(2000)
    }


    private fun observeDateSelected() {
        viewModel.monthAndYear.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            if(it.first == "-1"){
                timePeriod.text = getString(R.string.time_period,it.second)
            }else{
                timePeriod.text = getString(R.string.time_period,"${it.first}.${it.second}")
            }

        })
    }

    private fun observeStoreSelected() {
        viewModel.currentStore.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            selectStore.setText(it.storeName)
        })
    }

    private val changeDate: (month: Int, year: Int) -> Unit = {
            month, year ->
        viewModel.monthAndYear.value = Pair(getMonthWithZero(month), year.toString())
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

}