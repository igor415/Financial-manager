package com.varivoda.igor.tvz.financijskimanager.ui.bills

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Bill
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.util.MonthYearDialog
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
        argsText = args.text
        val view = inflater.inflate(R.layout.fragment_bill, container, false)
        when(args.text){
            "Zaposlenik koji je uprihodio najveću svotu novca po mjesecu" -> {
                view.billsRecyclerview.visibility = View.GONE
                view.header.visibility = View.GONE
                view.resultTextView.visibility = View.VISIBLE
            }
        }
        view.billsRecyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = billAdapter
        }
        return view
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

}