package com.varivoda.igor.tvz.financijskimanager.ui.invoice

import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.varivoda.igor.tvz.financijskimanager.App
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.util.MonthYearDialog
import com.varivoda.igor.tvz.financijskimanager.util.getMonthWithZero
import kotlinx.android.synthetic.main.fragment_best_invoice.*
import kotlinx.android.synthetic.main.invoice_template.*
import net.glxn.qrgen.android.QRCode

class BestInvoiceFragment : Fragment() {

    private val viewModel by viewModels<BestInvoiceViewModel> {
        BestInvoiceViewModelFactory((requireContext().applicationContext as App).billRepository,
            (requireContext().applicationContext as App).storeRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_best_invoice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changePeriod.setOnClickListener {
            MonthYearDialog().getDialog(activity as HomeActivity, changeDate)
        }
        selectStore.setOnClickListener {
            storeDialog()
        }
        observeCurrentStore()
        observeMonthValue()
        observeResult()
    }

    private fun observeResult() {
        viewModel.result.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            invoice.visibility = View.VISIBLE
            storeInfo.text = it.storeName
            timeInfo.text = "${it.date} ${it.time}"
            billInfoText.text = it.invoiceId.toString()
            employeeInfoText.text = "${it.name} ${it.surname}"
            total.text = "${it.total} kn"
            payment.text = it.paymentMethodName
            qrImage.setImageBitmap(getImageFromQrCode(it.invoiceId.toString()))
        })
    }

    private fun getImageFromQrCode(string: String): Bitmap? {
        return QRCode.from(string).withSize(360, 290).bitmap()
    }

    private fun observeMonthValue() {
        viewModel.monthAndYear.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            timePeriod.text = getString(R.string.time_period,"${it.first}.${it.second}")
        })
    }

    private val changeDate: (month: Int, year: Int) -> Unit = { month, year ->
        viewModel.monthAndYear.value = Pair(getMonthWithZero(month), year.toString())
    }

    private fun observeCurrentStore() {
        viewModel.currentStore.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            selectStore.setText(it.storeName)
        })
    }

    private var storeDialog: AlertDialog? = null
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