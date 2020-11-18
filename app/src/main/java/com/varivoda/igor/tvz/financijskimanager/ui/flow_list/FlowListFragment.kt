package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_flow_list.view.*
import kotlinx.android.synthetic.main.product_popup.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class FlowListFragment : Fragment() {

    private lateinit var flowListAdapter: FlowListAdapterProducts
    private lateinit var flowListAdapterEmployees: FlowListAdapterEmployees
    private lateinit var flowListAdapterCustomers: FlowListAdapterCustomers
    private val flowListAdapterStores = FlowListAdapterStores()
    private lateinit var flowListViewModel: FlowListViewModel
    private lateinit var flowListViewModelFactory: FlowListViewModelFactory
    private var productPopup: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_flow_list, container, false)
        val args = FlowListFragmentArgs.fromBundle(requireArguments())
        (activity as HomeActivity).setActionBarText(args.text)
        flowListViewModelFactory = FlowListViewModelFactory(requireContext())
        flowListViewModel = ViewModelProvider(this,flowListViewModelFactory).get(FlowListViewModel::class.java)
        flowListAdapterEmployees = FlowListAdapterEmployees(flowListViewModel)
        flowListAdapterCustomers = FlowListAdapterCustomers(flowListViewModel)
        flowListAdapter = FlowListAdapterProducts(changeProductInfo,flowListViewModel)
        when(args.text){
            "Popis proizvoda" ->{
                productsFunction(view)
            }
            "Popis zaposlenika" -> {
                employeesFunction(view)
            }
            "Popis kupaca" -> {
                customersFunction(view)
            }
            "Popis poslovnica" -> {
                storesFunction(view)
            }
        }

        return view
    }
    private val changeProductInfo: (Product) -> Unit = {
            item ->
        productPopup?.cancel()
        productPopup = addProductPopup("Change product info",item)
        productPopup?.show()
    }

    private fun storesFunction(view: View) {
        CoroutineScope(Dispatchers.Main).launch{
            flowListViewModel.stores.collect{
                flowListAdapterStores.submitList(it)
            }
        }
        view.flowListRecyclerView.apply {
            setHasFixedSize(true)
            adapter = flowListAdapterStores
        }
    }

    private fun customersFunction(view: View) {
        CoroutineScope(Dispatchers.Main).launch{
            flowListViewModel.customers.collect{
                flowListAdapterCustomers.submitList(it)
            }
        }
        view.flowListRecyclerView.apply {
            setHasFixedSize(true)
            adapter = flowListAdapterCustomers
        }
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(flowListAdapterCustomers,null,null,flowListViewModel))
        itemTouchHelper.attachToRecyclerView(view.flowListRecyclerView)
    }

    private fun employeesFunction(view: View) {

        CoroutineScope(Dispatchers.Main).launch{
            flowListViewModel.employees.collect{
                flowListAdapterEmployees.submitList(it)
            }
        }
        view.flowListRecyclerView.apply {
            setHasFixedSize(true)
            adapter = flowListAdapterEmployees
        }
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(null,flowListAdapterEmployees,null,flowListViewModel))
        itemTouchHelper.attachToRecyclerView(view.flowListRecyclerView)
    }

    private fun productsFunction(view: View) {

        CoroutineScope(Dispatchers.Main).launch{
            flowListViewModel.allProducts.collect{
                flowListAdapter.submitList(it)
            }
        }
        view.flowListRecyclerView.apply {
            setHasFixedSize(true)
            adapter = flowListAdapter
        }
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(null,null,flowListAdapter,flowListViewModel))
        itemTouchHelper.attachToRecyclerView(view.flowListRecyclerView)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.add_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.addButton){
            when(FlowListFragmentArgs.fromBundle(requireArguments()).text){
                "Popis proizvoda" -> {
                    productPopup?.cancel()
                    productPopup = addProductPopup("Insert product")
                    productPopup?.show()
                }
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addProductPopup(
        text: String,
        item: Product? = null
    ): AlertDialog? {
        val builder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.product_popup, LinearLayout(context), false)

        var edit = false
        if(item != null){
            dialogView.nameInput.setText(item.productName)
            dialogView.price_input.setText(item.price.toString())
            edit = true
        }

        dialogView.apply {
            product_popup_title.text = text
            cancel_button.setOnClickListener {
                productPopup?.dismiss()
            }
            ok_button.setOnClickListener {
                if(price_input.text.toString().isNotEmpty() && nameInput.text.toString().isNotEmpty()){
                    if(edit){
                        flowListViewModel.insertProduct(Product(item?.id!!, nameInput.text.toString(), price_input.text.toString().toDouble()))
                    }else{
                        flowListViewModel.insertProduct(Product(productName = nameInput.text.toString(),price = price_input.text.toString().toDouble()))
                    }
                    productPopup?.dismiss()
                }else{
                    if(price_input.text.toString().isEmpty()) price_input.error = context.getString(R.string.field_empty)
                    if(nameInput.text.toString().isEmpty()) nameInput.error = context.getString(R.string.field_empty)
                }
            }
        }
        return builder.setView(dialogView).create()
    }


}