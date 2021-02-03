package com.varivoda.igor.tvz.financijskimanager.ui.date_product

import android.app.AlertDialog
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
import kotlinx.android.synthetic.main.fragment_bar_chart.*
import kotlinx.android.synthetic.main.fragment_bar_chart.changePeriod
import kotlinx.android.synthetic.main.fragment_bar_chart.timePeriod
import kotlinx.android.synthetic.main.fragment_bill.*
import kotlinx.android.synthetic.main.fragment_bill.resultTextView
import kotlinx.android.synthetic.main.fragment_date_and_product.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DateAndProductFragment : Fragment() {

    private val dateAndProductViewModel by viewModels<DateAndProductViewModel> {
        DateAndProductViewModelFactory((requireContext().applicationContext as App).storeRepository,
            (requireContext().applicationContext as App).productRepository,
            (requireContext().applicationContext as App).employeeRepository)
    }
    private var productDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_date_and_product, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = DateAndProductFragmentArgs.fromBundle(requireArguments())
        when(args.text){
            "Poslovnica koja najbolje prodaje određeni proizvod" -> {
                observeResult()
            }
            "Zaposlenik koji je prodao najveću količinu nekog proizvoda po mjesecu" ->{
                observeEmployeeResult()
            }
        }
        observeDateChange()
        changePeriod.setOnClickListener {
            MonthYearDialog().getDialog(activity as HomeActivity,changeDate)
        }

        pickProduct.setOnClickListener { productDialog() }
        observeProductSelected()
    }

    private fun observeEmployeeResult() {
        dateAndProductViewModel.employeeResult.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            if(it==""){
                resultTextView.text = getString(R.string.no_data_for_month_and_year_for_product)
            }else{
                resultTextView.text = it
            }

        })
    }

    private fun observeProductSelected() {
        dateAndProductViewModel.productSelected.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            pickProduct.setText(it.productName)
        })
    }

    private fun observeResult() {
        dateAndProductViewModel.result.observe(viewLifecycleOwner, Observer {
            if(it==null){
                return@Observer
            }else if(it==""){
                resultTextView.text = getString(R.string.no_data_for_month_and_year_for_product)
            }else{
                resultTextView.text = it
            }
        })
    }

    private fun observeDateChange() {
        dateAndProductViewModel.dateSelected.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            timePeriod.text = getString(R.string.time_period,it)
        })
    }

    private val changeDate: (month: Int, year: Int) -> Unit = {
            month, year ->
        dateAndProductViewModel.dateSelected.value = getMonthAndYearFormatted(month, year)
    }

    private fun productDialog() {
        dateAndProductViewModel.getAllProducts()
        dateAndProductViewModel.products.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.select_product))
            builder.setItems(it.map { itt -> itt.productName }.toTypedArray()
            ) { dialog, which ->
                dateAndProductViewModel.insertProduct(it[which])
                productDialog?.cancel() }
            productDialog?.cancel()
            productDialog = builder.create()
            productDialog?.show()
            dateAndProductViewModel.products.value = null
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