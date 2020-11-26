package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.ItemTouchHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.databinding.ProductPopupBinding
import com.varivoda.igor.tvz.financijskimanager.ui.flow_list.loadstate.MyLoadStateAdapter
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_flow_list.view.*
import kotlinx.android.synthetic.main.product_popup.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlin.coroutines.CoroutineContext


class FlowListFragment : Fragment() {

    private lateinit var flowListAdapter: FlowListAdapterProducts
    private lateinit var flowListAdapterEmployees: FlowListAdapterEmployees
    private lateinit var flowListAdapterCustomers: FlowListAdapterCustomers
    private val flowListAdapterStores = FlowListAdapterStores()
    private lateinit var flowListViewModel: FlowListViewModel
    private lateinit var flowListViewModelFactory: FlowListViewModelFactory

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
        flowListViewModel.apply {
            productPopup?.cancel()
            productPopup = addProductPopup("Change product info",item)
            productPopup?.setCancelable(false)
            productPopup?.setCanceledOnTouchOutside(false)
            productPopup?.show()
        }

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

       /* CoroutineScope(Dispatchers.Main).launch{
            flowListViewModel.allProducts.collect{
                flowListAdapter.submitList(it)
            }
        }*/
        view.retry_button.setOnClickListener {
            flowListAdapter.retry()
        }
        flowListAdapter.withLoadStateHeaderAndFooter(
            header = MyLoadStateAdapter { flowListAdapter.retry() },
            footer = MyLoadStateAdapter { flowListAdapter.retry() }
        )

        flowListAdapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            view.flowListRecyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            view.progress_bar.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            view.retry_button.isVisible = loadState.source.refresh is LoadState.Error

        }
        getProductsStream()
        view.flowListRecyclerView.apply {
            setHasFixedSize(true)
            adapter = flowListAdapter
        }

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(null,null,flowListAdapter,flowListViewModel))
        itemTouchHelper.attachToRecyclerView(view.flowListRecyclerView)
    }

    private var searchJob: Job? = null

    private fun getProductsStream() {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {


                flowListViewModel.getProducts().collectLatest {
                    //delay(2000)
                    flowListAdapter.submitData(it)

            }

        }
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
                    flowListViewModel.apply {
                        productPopup?.cancel()
                        productPopup = addProductPopup("Insert product")
                        productPopup?.setCancelable(false)
                        productPopup?.setCanceledOnTouchOutside(false)
                        productPopup?.show()
                    }

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
        val binding: ProductPopupBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.product_popup, LinearLayout(context), false)
        //val dialogView = LayoutInflater.from(context)
          //  .inflate(R.layout.product_popup, LinearLayout(context), false)
        binding.viewModel = flowListViewModel
        flowListViewModel.edit = item != null
        if(item != null){
            //binding.item = item
            //binding.root.nameInput.setText(item.productName)
            //binding.root.price_input.setText(item.price.toString())
            flowListViewModel.nameInput = item.productName.split(":")[0]
            flowListViewModel.priceInput = item.price.toString()
            flowListViewModel.item = item
        }else{
            flowListViewModel.item = null
        }
        flowListViewModel.title = text



        /*binding.root.apply {
            //product_popup_title.text = text
            /*cancel_button.setOnClickListener {
                productPopup?.dismiss()
                flowListViewModel.clearProductInfo()
            }*/
            /*ok_button.setOnClickListener {
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
            }*/
        }*/
        return builder.setView(binding.root).create()
    }


}