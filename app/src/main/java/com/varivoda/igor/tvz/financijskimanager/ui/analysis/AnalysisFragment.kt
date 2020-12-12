package com.varivoda.igor.tvz.financijskimanager.ui.analysis

import android.app.AlertDialog
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.varivoda.igor.tvz.financijskimanager.App
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.model.StatisticsEntry
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.util.MonthYearDialog
import com.varivoda.igor.tvz.financijskimanager.util.getMonthAndYearFormatted
import kotlinx.android.synthetic.main.fragment_analysis.*


class AnalysisFragment : Fragment() {

    private val analysisViewModel by viewModels<AnalysisViewModel> {
        AnalysisViewModelFactory((requireContext().applicationContext as App).productRepository)
    }
    private var productDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_analysis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeActivity).setActionBarText(AnalysisFragmentArgs.fromBundle(requireArguments()).text)
        onBtnFullScreenClicked()
        btnSalesPointSelected.setOnClickListener {
            productDialog()
        }
        fullDate.setOnClickListener {
            MonthYearDialog().getDialog((activity as HomeActivity),changeDate)
        }
        firstRadioButton.setOnClickListener {
            handleRadioButtonClick(firstRadioButton)
        }
        secondRadioButton.setOnClickListener {
            handleRadioButtonClick(secondRadioButton)
        }
        //initial
            analysisViewModel.setSelectionFlag(1)
            filterLayout.visibility = View.GONE
            btnFullScreen.text = getString(R.string.show_filters)
            btnFullScreen.setTextColor(requireContext().getColor(R.color.colorPrimary))

        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            filterLayout.visibility = View.GONE
            btnFullScreen.visibility = View.GONE
        }
        observeDateSelected()
        reloadGraph()
        observeStatisticsData()
        observeFirstAndSecondProduct()
        removeProductSelection()
    }


    private fun removeProductSelection() {
        etSelectedSalesPoint.setOnClickListener{
            analysisViewModel.deleteProduct()
            btnSalesPointSelected.setText("")
            etSelectedSalesPoint.visibility = View.GONE
            reloadGraph()
        }
    }

    private fun observeFirstAndSecondProduct() {
        analysisViewModel.firstProduct.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            btnSalesPointSelected.setText(it.productName)
            etSelectedSalesPoint.visibility = View.VISIBLE

        })

        analysisViewModel.secondProduct.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            btnSalesPointSelected.setText(it.productName)
        })
    }

    private fun observeStatisticsData() {
        analysisViewModel.firstProductStatistics.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            reloadGraph()
        })

        analysisViewModel.secondProductStatistics.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer

            reloadGraph()
        })
    }

    private fun observeDateSelected() {
        analysisViewModel.dateSelected.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            fullDate.setText(it)
        })
    }

    private fun reloadGraph() {
        val firstEntries = mutableListOf<Entry>()
        analysisViewModel.firstProductStatistics.value?.forEach { stat ->
            firstEntries.add(Entry(stat.day.toFloat(),stat.total.toFloat(),stat.day))
        }

        val firstDataSet = LineDataSet(
            firstEntries,
            getString(R.string.first_selection)
        )
        firstDataSet.color = requireContext().getColor(R.color.colorPrimary)
        firstDataSet.lineWidth = 2.0f
        val secondEntries = mutableListOf<Entry>()
        analysisViewModel.secondProductStatistics.value?.forEach { stat ->
            secondEntries.add(Entry(stat.day.toFloat(),stat.total.toFloat()))
        }
        val secondDataSet = LineDataSet(
            secondEntries,
            getString(R.string.second_selection)
        )
        secondDataSet.color = requireContext().getColor(R.color.colorAccent)
        secondDataSet.lineWidth = 2.0f

        val dataSet = ArrayList<ILineDataSet>()
        dataSet.add(firstDataSet)
        dataSet.add(secondDataSet)

        val data = LineData(dataSet)
        chart.data = data
        chart.axisLeft.isEnabled = true
        chart.axisRight.isEnabled = false
        chart.description.isEnabled = false
        chart.invalidate()
    }

    private fun handleRadioButtonClick(rb: RadioButton) {
        val isSelected = rb.isChecked
        when (rb.id) {
            R.id.firstRadioButton -> if (isSelected) {

                firstRadioButton.setTextColor(Color.WHITE)
                secondRadioButton.setTextColor(Color.BLUE)
                btnSalesPointSelected.setText(analysisViewModel.firstProduct.value?.productName ?: "")
                if (btnSalesPointSelected.text.toString() == "") {
                    etSelectedSalesPoint.visibility = View.GONE
                } else {
                    etSelectedSalesPoint.visibility = View.VISIBLE
                }
                analysisViewModel.setSelectionFlag(1)

            }
            R.id.secondRadioButton -> if (isSelected) {

                secondRadioButton.setTextColor(Color.WHITE)
                firstRadioButton.setTextColor(Color.BLUE)
                btnSalesPointSelected.setText(analysisViewModel.secondProduct.value?.productName ?: "")
                if (btnSalesPointSelected.text.toString() == "") {
                    etSelectedSalesPoint.visibility = View.GONE
                } else {
                    etSelectedSalesPoint.visibility = View.VISIBLE
                }
                analysisViewModel.setSelectionFlag(2)
            }
        }
    }
    

    private val changeDate: (month: Int, year: Int) -> Unit = { month, year ->
        analysisViewModel.dateSelected.value = getMonthAndYearFormatted(month,year)
    }

    private fun productDialog() {
        analysisViewModel.getAllProducts()
        analysisViewModel.products.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.select_product))
            builder.setItems(it.map { itt -> itt.productName }.toTypedArray()
            ) { dialog, which ->
                analysisViewModel.insertProduct(it[which])
                dialog?.dismiss() }
            productDialog?.cancel()
            productDialog = builder.create()
            productDialog?.show()
            analysisViewModel.products.value = null
        })

    }

    private fun onBtnFullScreenClicked() {
        btnFullScreen.setOnClickListener {

            if (btnFullScreen.text == getString(R.string.show_filters)) {
                filterLayout.visibility = View.VISIBLE
                btnFullScreen.text = getString(R.string.hide_filters)
                btnFullScreen.setTextColor(requireContext().getColor(R.color.colorAccent))
            } else {
                filterLayout.visibility = View.GONE
                btnFullScreen.text = getString(R.string.show_filters)
                btnFullScreen.setTextColor(requireContext().getColor(R.color.colorPrimary))
            }
        }
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