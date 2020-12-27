package com.varivoda.igor.tvz.financijskimanager.ui.stock

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.varivoda.igor.tvz.financijskimanager.App
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.model.ProductStockDTO
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.ui.maps.REQUEST_LOCATION_PERMISSION
import com.varivoda.igor.tvz.financijskimanager.util.closeKeyboard
import com.varivoda.igor.tvz.financijskimanager.util.openKeyboard
import com.varivoda.igor.tvz.financijskimanager.util.toast
import kotlinx.android.synthetic.main.fragment_stock.*
import kotlinx.android.synthetic.main.fragment_stock.view.*
import kotlinx.android.synthetic.main.header_dark.*
import kotlinx.android.synthetic.main.info_dialog.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

const val REQUEST_PHONE_PERMISSION = 11
class StockFragment : Fragment() {

    private val viewModel by viewModels<StockViewModel> {
        StockViewModelFactory((requireContext().applicationContext as App).storeRepository)
    }
    private lateinit var stockAdapter: StockAdapter
    private var infoDialog: AlertDialog? = null
    private var currentProductName: String? = null
    private var phonePermission = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_stock, container, false)
        stockAdapter = StockAdapter(showInfo, false)
        view.product_recycler.adapter = stockAdapter
        //(activity as HomeActivity).removeActionBar()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeStockData()
        searchFeature()
        observeInfoResult()
    }

    private fun observeInfoResult() {
        viewModel.info.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            infoDialog(it)
        })
    }

    private val showInfo: (ProductStockDTO) -> Unit = { item ->
        viewModel.getInfo(item.productId)
        currentProductName = item.productName
    }

    override fun onStart() {
        super.onStart()
        (activity as HomeActivity).removeActionBar()
    }

    override fun onPause() {
        super.onPause()
        (activity as HomeActivity).showActionBar()
    }

    private fun searchFeature() {
        searchButton.setOnClickListener {
            if (searchEditText.visibility == View.VISIBLE) {
                searchButton.setImageResource(R.drawable.ic_search_gray)
                searchEditText.setText("")
                searchEditText.closeKeyboard(context)
                searchEditText.visibility = View.GONE
            } else {
                searchMode()
            }

        }
    }

    private fun searchMode() {
        searchButton.setImageResource(R.drawable.ic_close_gray)
        searchEditText.visibility = View.VISIBLE
        searchEditText.requestFocus()
        searchEditText.openKeyboard(context)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length > 1) {
                    val editedText = s.toString().toLowerCase(Locale.getDefault())
                    viewModel.filter.value = editedText
                } else if (s != null && s.isEmpty()) {
                    viewModel.filter.value = ""
                }
            }
        })
    }

    private fun observeStockData() {
        viewModel.allProducts.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            stockAdapter.submitList(it)
            println("debug data is $it")
            progress_bar.visibility = View.GONE
        })
    }

    private fun infoDialog(it: List<ProductStockDTO>) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.info_dialog, null, false)
        dialogView.product_popup_title.text = currentProductName ?: ""
        dialogView.ok_button.setOnClickListener {
            infoDialog?.cancel()
        }
        val adapter = StockAdapter(callStore, true)
        dialogView.infoList.layoutManager = LinearLayoutManager(context)
        dialogView.infoList.adapter = adapter
        adapter.submitList(it)
        builder.setView(dialogView)
        infoDialog?.cancel()
        infoDialog = builder.create()
        infoDialog?.show()

    }

    private val callStore: (ProductStockDTO) -> Unit = { item ->
        askForPermission()
        if(phonePermission) {
            lifecycleScope.launch(Dispatchers.IO) {
                val number = viewModel.getNumberForStoreId(item.idStore)
                withContext(Dispatchers.Main) {
                    if (number != null) {
                        val callIntent = Intent(Intent.ACTION_CALL)
                        callIntent.data = Uri.parse("tel:$number")
                        startActivity(callIntent)
                    } else {
                        context.toast("There is problem with phone number.")
                    }

                }
            }
        }else{
            context.toast("You need to give permission for phone call.")
        }


    }

    private fun askForPermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_PHONE_PERMISSION
            )
        }else{
            phonePermission = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == REQUEST_PHONE_PERMISSION){
            if(grantResults.contains(PackageManager.PERMISSION_GRANTED)){
                askForPermission()
            }
        }
    }

}