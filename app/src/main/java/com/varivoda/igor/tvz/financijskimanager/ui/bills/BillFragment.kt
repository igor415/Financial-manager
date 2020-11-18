package com.varivoda.igor.tvz.financijskimanager.ui.bills

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.util.MonthYearDialog
import com.varivoda.igor.tvz.financijskimanager.util.getCurrentYear
import com.varivoda.igor.tvz.financijskimanager.util.getMonthAndYearFormatted
import com.varivoda.igor.tvz.financijskimanager.util.getMonthWithZero
import kotlinx.android.synthetic.main.fragment_bill.*
import kotlinx.android.synthetic.main.fragment_bill.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BillFragment : Fragment() {

    private lateinit var billViewModelFactory: BillViewModelFactory
    private lateinit var billViewModel: BillViewModel
    private lateinit var argsText: String
    private var billAdapter: BillAdapter = BillAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = BillFragmentArgs.fromBundle(requireArguments())
        (activity as HomeActivity).setActionBarText(args.text)
        billViewModelFactory = BillViewModelFactory(requireContext())
        billViewModel = ViewModelProvider(requireActivity(),billViewModelFactory).get(BillViewModel::class.java)
        argsText = args.text
        val view = inflater.inflate(R.layout.fragment_bill, container, false)
        when(args.text){
            "Zaposlenik koji je uprihodio najveću svotu novca po mjesecu" -> {
                adjustLayout(view)
                monthAndYear(view)
            }
            "Prodavač koji je najviše dana u godini izdao račun" -> {
                adjustLayout(view)
                onlyYear(view)
                observeEmployeeMostDaysIssuedInvoice()
            }

        }
        view.billsRecyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = billAdapter
        }
        return view
    }

    private fun observeEmployeeMostDaysIssuedInvoice() {
        billViewModel.employeeInvoiceNumberOfDays.observe(viewLifecycleOwner, Observer {
            if(it==null) {
                resultTextView.text = getString(R.string.no_data_for_year)
            }
            resultTextView.text = it
        })
    }

    private fun adjustLayout(view: View) {
        view.billsRecyclerview.visibility = View.GONE
        view.header.visibility = View.GONE
        view.resultTextView.visibility = View.VISIBLE
    }

    private fun monthAndYear(view: View){
        view.timePeriod.text = getString(R.string.time_period, getMonthAndYearFormatted())
        view.changePeriod.setOnClickListener {
            MonthYearDialog().getDialog(activity as HomeActivity,changeDate)
        }
    }

    private fun onlyYear(view: View){
        view.timePeriod.text = getString(R.string.time_period, getCurrentYear())
        view.changePeriod.setOnClickListener {
            MonthYearDialog().getOnlyYearDialog(activity as HomeActivity,changeYear)
        }
    }


    private val changeDate: (month: Int, year: Int) -> Unit = {
        month, year ->
        when(argsText) {
            "Zaposlenik koji je uprihodio najveću svotu novca po mjesecu" -> {
                CoroutineScope(Dispatchers.Main).launch {
                    val result = billViewModel.getEmployeeTotalPerMonthAndYear(getMonthWithZero(month),year.toString())
                    if(result == null) {
                        resultTextView.text = getString(R.string.no_data_for_month_and_year)
                    }else{
                        resultTextView.text = result
                    }
                }
            }
            "Popis računa" ->{
                billViewModel.getBills(getMonthWithZero(month),year.toString()).observe(viewLifecycleOwner,
                    Observer {
                        if(it==null) return@Observer
                        billAdapter.setItems(it)
                    })
            }

        }

        timePeriod.text = getString(R.string.time_period, getMonthAndYearFormatted(month,year))
    }

    private val changeYear: (year: Int) -> Unit = {
        year ->
        when(argsText){
            "Prodavač koji je najviše dana u godini izdao račun" -> {
                billViewModel.getEmployeeMostDaysIssuedInvoice(year.toString())
            }
        }
        timePeriod.text = getString(R.string.time_period,year.toString())
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