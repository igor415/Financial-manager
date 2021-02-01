package com.varivoda.igor.tvz.financijskimanager.ui.check_inventory

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.varivoda.igor.tvz.financijskimanager.App
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.inventory.InventoryViewModel
import com.varivoda.igor.tvz.financijskimanager.ui.inventory.InventoryViewModelFactory
import com.varivoda.igor.tvz.financijskimanager.util.*
import kotlinx.android.synthetic.main.fragment_check_inventory.*

const val STORAGE_REQUEST_CODE = 12
class CheckInventoryFragment : Fragment() {

    private var storeDialog: AlertDialog? = null
    private var checkInventoryAdapter = CheckInventoryAdapter()
    private val inventoryViewModel by viewModels<InventoryViewModel> {
        InventoryViewModelFactory((requireContext().applicationContext as App).inventoryRepository,
            (requireContext().applicationContext as App).storeRepository,
            (requireContext().applicationContext as App).productRepository,
            (requireContext().applicationContext as App).preferences)
    }
    private lateinit var dateListener: DatePickerDialog.OnDateSetListener


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_inventory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_REQUEST_CODE)
        time.text = getInventoryDate()
        name.text = "test test"
        recyclerView.adapter = checkInventoryAdapter
        observeStockData()
        confirm.setOnClickListener {
            if(signature_pad.isEmpty){
                context?.toast(getString(R.string.siganture))
            }else{
                val bm = signature_pad.signatureBitmap
                inventoryViewModel.executeInventory(checkInventoryAdapter.items.toDomainModel())
                PdfDocumentInventory().createPdf(bm,resources, checkInventoryAdapter.items,"test test", requireContext())
                signature_pad.clear()
                signature_pad.clearView()
            }
        }
        inventoryViewModel.infoMessage.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            showSelectedToast(requireContext(), it)
        })
       /* pickStore.setOnClickListener {
            storeDialog()
        }
        confirmButton.setOnClickListener {
            if(inventoryViewModel.checkIfInfoInserted() && fullName.text.isNotEmpty()){
                enterInfo.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    progressBar.visibility = View.GONE
                }, 500)

            }else{
                context.toast(getString(R.string.enter_info))
            }
        }
        dateListener =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                pickDate.setText(getFullMonthFormatted(dayOfMonth,month,year))
                inventoryViewModel.pickedDate = getFullMonthFormatted(dayOfMonth,month,year)
            }
        pickDate.setText(getTodayDate())
        inventoryViewModel.pickedDate = getTodayDate()
        pickDate.setOnClickListener {
            MyDatePicker().openDialog(activity as HomeActivity, dateListener)
        }*/
    }

    private fun observeStockData() {
        inventoryViewModel.getStockData()
        inventoryViewModel.stockData.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            checkInventoryAdapter.setItemsValue(it)
        })
    }

    /* private fun storeDialog() {
         inventoryViewModel.getStores()
         inventoryViewModel.stores.observe(viewLifecycleOwner, Observer {
             if(it==null) return@Observer
             val builder: AlertDialog.Builder = AlertDialog.Builder(context)
             builder.setTitle(getString(R.string.select_product))
             builder.setItems(it.map { itt -> itt.storeName }.toTypedArray()
             ) { dialog, which ->
                 inventoryViewModel.pickedStore = it[which]
                 pickStore.setText(it[which].storeName)
                 dialog?.dismiss() }
             storeDialog?.cancel()
             storeDialog = builder.create()
             storeDialog?.show()
             inventoryViewModel.stores.value = null
         })

     }*/

    override fun onPause() {
        super.onPause()
        signature_pad.removeCallbacks {  }
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