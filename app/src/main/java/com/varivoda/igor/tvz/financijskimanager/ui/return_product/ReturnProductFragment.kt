package com.varivoda.igor.tvz.financijskimanager.ui.return_product

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.varivoda.igor.tvz.financijskimanager.App
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.util.closeKeyboard
import com.varivoda.igor.tvz.financijskimanager.util.getImageFromQrCode
import com.varivoda.igor.tvz.financijskimanager.util.showSelectedToast
import com.varivoda.igor.tvz.financijskimanager.util.toast
import kotlinx.android.synthetic.main.fragment_return_product.*
import kotlinx.android.synthetic.main.invoice_template.view.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.text.SimpleDateFormat
import java.util.*


class ReturnProductFragment : Fragment(), ZXingScannerView.ResultHandler {

    private val viewModel by viewModels<ReturnProductViewModel> {
        ReturnProductViewModelFactory((requireContext().applicationContext as App).billRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_return_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpScanner()
        confirm.setOnClickListener {
            if(invoiceNumberEntry.text.isNotEmpty()){
                invoiceNumberEntry.closeKeyboard(context)
                stopScan()
                confirmDialog()
            }else{
                context.toast(getString(R.string.enter_invoice_num))
            }
        }
        observeResult()
    }
    private var confirmDialog: AlertDialog? = null
    @SuppressLint("SetTextI18n")
    private fun confirmDialog() {
        viewModel.getInvoiceInfo(invoiceNumberEntry.text.toString())
        viewModel.invoiceInfo.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            val dialogView = LayoutInflater.from(context).inflate(R.layout.invoice_template,null,false)
            dialogView.storeInfo.text = it.storeName
            dialogView.timeInfo.text = "${it.date} ${it.time}"
            dialogView.billInfoText.text = it.invoiceId.toString()
            dialogView.employeeInfoText.text = "${it.name} ${it.surname}"
            dialogView.total.text = "${it.total} kn"
            dialogView.payment.text = it.paymentMethodName
            dialogView.confirmButton.visibility = View.VISIBLE
            dialogView.confirmButton.setOnClickListener { view ->
                if(checkDays(it.date)){
                    viewModel.returnItem(invoiceNumberEntry.text.toString())
                    confirmDialog?.cancel()
                }else{
                    showSelectedToast(requireContext(), getString(R.string.seven_days_purchase))
                }

            }
            dialogView.qrImage.setImageBitmap(getImageFromQrCode(it.invoiceId.toString()))
            builder.setView(dialogView)
            confirmDialog?.cancel()
            confirmDialog = builder.create()
            confirmDialog?.show()
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun checkDays(date: String): Boolean{
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val fromDate: Date? = sdf.parse(date)
        val toDate = Calendar.getInstance()
        val day = toDate.get(Calendar.DAY_OF_MONTH)
        val month = toDate.get(Calendar.MONTH)+1
        val year = toDate.get(Calendar.YEAR)
        val todate: Date? = sdf.parse("$year-$month-$day")
        val c = Calendar.getInstance()
        c.setTime(fromDate)
        c.add(Calendar.DATE,7)
        return c.getTime().compareTo(todate) >= 0
    }

    private fun observeResult() {
        viewModel.returnResult.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            if(it){
                showSelectedToast(requireContext(),getString(R.string.returned_item_success))
                findNavController().popBackStack()
            }else{
                showSelectedToast(requireContext(),getString(R.string.problem_returning))
            }
        })
    }

    override fun onStart() {
        super.onStart()
        startScan()
    }


    private var mScannerView: ZXingScannerView? = null

    private fun startScan() {
        mScannerView?.setResultHandler(this)
        mScannerView?.startCamera()
        mScannerView?.setAutoFocus(true)
    }

    private fun setUpScanner() {
        val contentFrame = content_barcode_scanner
        mScannerView = ZXingScannerView(activity)
        mScannerView?.setBorderCornerRadius(10)
        contentFrame.addView(mScannerView)
    }

    private fun stopScan() = mScannerView?.stopCamera()

    override fun handleResult(rawResult: com.google.zxing.Result?) {
        val serial = rawResult?.text
        serial ?: return
        invoiceNumberEntry.setText(serial)
        stopScan()

    }

    override fun onPause() {
        super.onPause()
        stopScan()
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