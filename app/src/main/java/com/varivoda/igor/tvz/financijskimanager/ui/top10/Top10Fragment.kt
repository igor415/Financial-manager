package com.varivoda.igor.tvz.financijskimanager.ui.top10

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.util.MonthYearDialog
import com.varivoda.igor.tvz.financijskimanager.util.getMonthAndYearFormatted
import com.varivoda.igor.tvz.financijskimanager.util.getMonthWithZero
import kotlinx.android.synthetic.main.fragment_bill.*
import kotlinx.android.synthetic.main.fragment_bill.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Top10Fragment : Fragment() {

    private lateinit var top10ViewModel: Top10ViewModel
    private lateinit var top10ViewModelFactory: Top10ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val args = Top10FragmentArgs.fromBundle(requireArguments())
        if(args.text=="Top 10 najprodavanijih proizvoda"){
            (activity as HomeActivity).setActionBarText(getString(R.string.top_products))
        }
        val view = inflater.inflate(R.layout.fragment_top10, container, false)
        view.timePeriod.text = getString(R.string.time_period, getMonthAndYearFormatted())
        view.changePeriod.setOnClickListener {
            MonthYearDialog().getDialog(activity as HomeActivity,changeDate)
        }
        top10ViewModelFactory = Top10ViewModelFactory(requireContext())
        top10ViewModel = ViewModelProvider(requireActivity(),top10ViewModelFactory).get(Top10ViewModel::class.java)
        observeTopProducts()
        return view
    }

    private fun observeTopProducts() {
        top10ViewModel.topProducts.observe(viewLifecycleOwner, Observer {
            if(it==null){

            }else{
                println("debug 10 products: $it")
            }
        })
    }


    private val changeDate: (month: Int, year: Int) -> Unit = {
            month, year ->
        top10ViewModel.monthAndYear.value = Pair(getMonthWithZero(month),year.toString())
        timePeriod.text = getString(R.string.time_period, getMonthAndYearFormatted(month,year))
    }


}