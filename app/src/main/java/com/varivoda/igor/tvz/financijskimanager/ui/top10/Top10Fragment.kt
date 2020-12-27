package com.varivoda.igor.tvz.financijskimanager.ui.top10

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.varivoda.igor.tvz.financijskimanager.App
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.util.MonthYearDialog
import com.varivoda.igor.tvz.financijskimanager.util.getMonthAndYearFormatted
import com.varivoda.igor.tvz.financijskimanager.util.getMonthWithZero
import kotlinx.android.synthetic.main.fragment_bill.timePeriod
import kotlinx.android.synthetic.main.fragment_bill.view.changePeriod
import kotlinx.android.synthetic.main.fragment_bill.view.timePeriod
import kotlinx.android.synthetic.main.fragment_top10.view.*
import kotlinx.android.synthetic.main.fragment_top3.*
import timber.log.Timber


class Top10Fragment : Fragment() {

    private val top10ViewModel by viewModels<Top10ViewModel> {
        Top10ViewModelFactory((requireContext().applicationContext as App).productRepository)
    }
    private var top10Adapter = Top10Adapter(Top10Listener {
        Timber.d("debug: $it")
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_top10, container, false)
        val args = Top10FragmentArgs.fromBundle(requireArguments())
        if(args.text=="Top 10 najprodavanijih proizvoda"){
            (activity as HomeActivity).setActionBarText(getString(R.string.top_products))
        }

        view.timePeriod.text = getString(R.string.time_period, getMonthAndYearFormatted())
        view.changePeriod.setOnClickListener {
            if(useMonthAndYearSwitch.isChecked) {
                MonthYearDialog().getDialog(activity as HomeActivity, changeDate)
            }else{
                MonthYearDialog().getOnlyYearDialog(activity as HomeActivity, changeYear)
            }
        }
        observeTopProducts()
        view.topRecyclerView.adapter = top10Adapter
        return view
    }


    private fun observeTopProducts() {
        top10ViewModel.topProducts.observe(viewLifecycleOwner, Observer {
            if(it==null){
                top10Adapter.setListValue(listOf())
            }else{
                top10Adapter.setListValue(it)
            }
        })
    }


    private val changeDate: (month: Int, year: Int) -> Unit = {
            month, year ->
        top10ViewModel.monthAndYear.value = Pair(getMonthWithZero(month),year.toString())
        timePeriod.text = getString(R.string.time_period, getMonthAndYearFormatted(month,year))
    }

    private val changeYear: (year: Int) -> Unit = {
            year ->
        top10ViewModel.monthAndYear.value = Pair("-1",year.toString())
        timePeriod.text = getString(R.string.time_period, year.toString())
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