package com.varivoda.igor.tvz.financijskimanager.ui.quarter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.model.ProductQuarterDTO
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.util.MonthYearDialog
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentYear
import kotlinx.android.synthetic.main.fragment_quarter.*
import kotlinx.android.synthetic.main.fragment_quarter.changePeriod
import kotlinx.android.synthetic.main.fragment_quarter.timePeriod
import java.util.*


class QuarterFragment : Fragment() {

    private lateinit var quarterViewModel: QuarterViewModel
    private lateinit var quarterViewModelFactory: QuarterViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quarter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        quarterViewModelFactory = QuarterViewModelFactory(requireContext())
        quarterViewModel = ViewModelProvider(this,quarterViewModelFactory).get(QuarterViewModel::class.java)
        observeProductPerQuarters()
        timePeriod.text = getString(R.string.time_period, getCurrentYear())
        changePeriod.setOnClickListener {
            MonthYearDialog().getDialog(activity as HomeActivity,changeDate)
        }
        quarterViewModel.getProductPerQuarter(getCurrentYear())
        quarterView.setOnClickListener {
            resolveQuarterValue(quarterViewModel.productsPerQuarter.value!!)
        }

    }

    private fun observeProductPerQuarters() {
        quarterViewModel.productsPerQuarter.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            resolveQuarterValue(it)
        })
    }

    private fun resolveQuarterValue(it: List<ProductQuarterDTO>) {
        var isShown = false
        when(quarterView.getCurrentQuarterValue().toLowerCase(Locale.ROOT)){
            "first" -> {
                if(it.stream().anyMatch{ item -> item.quarter == "first"}){
                    isShown = true
                    resultTextView.text = it.single { item -> item.quarter == "first" }.productName
                }
            }
            "second" -> {
                if(it.stream().anyMatch{ item -> item.quarter == "second"}){
                    isShown = true
                    resultTextView.text = it.single { item -> item.quarter == "second" }.productName
                }
            }
            "third" -> {
                if(it.stream().anyMatch{ item -> item.quarter == "third"}){
                    isShown = true
                    resultTextView.text = it.single { item -> item.quarter == "third" }.productName
                }
            }
            "fourth" -> {
                if(it.stream().anyMatch{ item -> item.quarter == "fourth"}){
                    isShown = true
                    resultTextView.text = it.single { item -> item.quarter == "fourth" }.productName
                }
            }
        }
        if(!isShown) resultTextView.text = "Nema podataka za ovaj kvartal"
    }

    private val changeDate: (month: Int, year: Int) -> Unit = {
            month, year ->
        quarterViewModel.getProductPerQuarter(year.toString())
        timePeriod.text = getString(R.string.time_period, year.toString())
    }

}