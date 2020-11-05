package com.varivoda.igor.tvz.financijskimanager.ui.bills

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.util.MonthYearDialog
import com.varivoda.igor.tvz.financijskimanager.util.getMonthAndYearFormatted
import com.varivoda.igor.tvz.financijskimanager.util.getMonthWithZero
import kotlinx.android.synthetic.main.fragment_bill.*


class BillFragment : Fragment() {

    private lateinit var billViewModelFactory: BillViewModelFactory
    private lateinit var billViewModel: BillViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = BillFragmentArgs.fromBundle(requireArguments())
        (activity as HomeActivity).setActionBarText(args.text)
        return inflater.inflate(R.layout.fragment_bill, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timePeriod.text = getString(R.string.time_period, getMonthAndYearFormatted())
        changePeriod.setOnClickListener {
            MonthYearDialog().getDialog(activity as HomeActivity,changeDate)
        }
        billViewModelFactory = BillViewModelFactory(requireContext())
        billViewModel = ViewModelProvider(requireActivity(),billViewModelFactory).get(BillViewModel::class.java)

    }

    private val changeDate: (month: Int, year: Int) -> Unit = {
        month, year ->
        billViewModel.getBills(getMonthWithZero(month),year.toString())
        timePeriod.text = getString(R.string.time_period, getMonthAndYearFormatted(month,year))
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