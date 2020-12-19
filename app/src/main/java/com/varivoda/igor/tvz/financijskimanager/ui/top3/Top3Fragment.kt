package com.varivoda.igor.tvz.financijskimanager.ui.top3

import android.app.AlertDialog
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.varivoda.igor.tvz.financijskimanager.App
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.util.*
import kotlinx.android.synthetic.main.fragment_horizontal_bar_chart.*
import kotlinx.android.synthetic.main.fragment_horizontal_bar_chart.changePeriod
import kotlinx.android.synthetic.main.fragment_horizontal_bar_chart.timePeriod
import kotlinx.android.synthetic.main.fragment_top3.*
import kotlinx.android.synthetic.main.fragment_top3.view.*


class Top3Fragment : Fragment() {

    private val viewModel by viewModels<Top3ViewModel> {
        Top3ViewModelFactory((requireContext().applicationContext as App).productRepository,
            (requireContext().applicationContext as App).storeRepository)
    }
    private val top3Adapter = Top3Adapter()
    private var storeDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_top3, container, false)
        view.recyclerViewTopThree.layoutManager = LinearLayoutManager(context)
        view.recyclerViewTopThree.adapter = top3Adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar.visibility = View.VISIBLE
        changePeriod.setOnClickListener {
            if(useMonthAndYearSwitch.isChecked) {
                MonthYearDialog().getDialog(activity as HomeActivity, changeDate)
            }else{
                MonthYearDialog().getOnlyYearDialog(activity as HomeActivity, changeYear)
            }
        }
        observeTop3Categories()
        pickStore.setOnClickListener { storeDialog() }
        observeStoreSelected()
        observeDateSelected()
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
            pickStore.setText(it.storeName)
        })
    }


    private fun observeTop3Categories() {
        viewModel.top3Categories.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            progressBar.visibility = View.GONE
            top3Adapter.setListAndInvalidate(it)

        })
    }

    private val changeYear: (year: Int) -> Unit = {
            year ->
        viewModel.monthAndYear.value = Pair("-1",year.toString())
    }

    private val changeDate: (month: Int, year: Int) -> Unit = {
            month, year ->
        progressBar.visibility = View.VISIBLE
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

    override fun onResume() {
        super.onResume()
        if(activity != null) requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onPause() {
        super.onPause()
        if(activity != null) requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }
}