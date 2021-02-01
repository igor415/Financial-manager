package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.ItemTouchHelper
import com.varivoda.igor.tvz.financijskimanager.App
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.databinding.EmployeeItemBinding
import com.varivoda.igor.tvz.financijskimanager.databinding.ProductPopupBinding
import com.varivoda.igor.tvz.financijskimanager.model.EmployeeDTO
import com.varivoda.igor.tvz.financijskimanager.ui.flow_list.loadstate.MyLoadStateAdapter
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.util.showSelectedToast
import kotlinx.android.synthetic.main.fragment_flow_list.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow


class FlowListFragment : Fragment() {

    private lateinit var flowListAdapter: FlowListAdapterProducts
    private lateinit var flowListAdapterEmployees: FlowListAdapterEmployees
    private lateinit var flowListAdapterCustomers: FlowListAdapterCustomers
    private val flowListAdapterStores = FlowListAdapterStores()

    private val flowListViewModel by viewModels<FlowListViewModel> {
        FlowListViewModelFactory((requireContext().applicationContext as App).storeRepository,
            (requireContext().applicationContext as App).productRepository,
            (requireContext().applicationContext as App).employeeRepository,
            (requireContext().applicationContext as App).customerRepository,
            (requireContext().applicationContext as App).preferences)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_flow_list, container, false)
        val args = FlowListFragmentArgs.fromBundle(requireArguments())
        (activity as HomeActivity).setActionBarText(args.text)
        flowListAdapterEmployees = FlowListAdapterEmployees(changeEmployeeInfo, flowListViewModel)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flowListViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            showSelectedToast(requireContext(),it)
            flowListViewModel.errorMessage.value = null
        })
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

    private val changeEmployeeInfo: (EmployeeDTO) -> Unit = {
            item ->
        flowListViewModel.apply {
            employeePopup?.cancel()
            employeePopup = addEmployeePopup("Change employee info",item)
            employeePopup?.setCancelable(false)
            employeePopup?.setCanceledOnTouchOutside(false)
            employeePopup?.show()
        }

    }

    private fun storesFunction(view: View) {
        progressAndRetryInvisible(view)
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

    var customerJob: Job? = null
    private fun customersFunction(view: View) {
       /* CoroutineScope(Dispatchers.Main).launch{
            flowListViewModel.customers.collect{
                flowListAdapterCustomers.submitList(it)
            }
        }*/
        customerJob?.cancel()
        customerJob = lifecycleScope.launch {


            flowListViewModel.getCustomersPaging().collectLatest {
                //delay(2000)
                flowListAdapterCustomers.submitData(it)

            }

        }

        flowListAdapterCustomers.withLoadStateHeaderAndFooter(
            header = MyLoadStateAdapter { flowListAdapter.retry() },
            footer = MyLoadStateAdapter { flowListAdapter.retry() }
        )

        flowListAdapterCustomers.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            view.flowListRecyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            view.progress_bar.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            view.retry_button.isVisible = loadState.source.refresh is LoadState.Error

        }
        view.flowListRecyclerView.apply {
            setHasFixedSize(true)
            adapter = flowListAdapterCustomers
        }
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(flowListAdapterCustomers,null,null,flowListViewModel))
        itemTouchHelper.attachToRecyclerView(view.flowListRecyclerView)
    }

    private fun employeesFunction(view: View) {

        progressAndRetryInvisible(view)
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

    fun progressAndRetryInvisible(view: View){
        view.progress_bar.visibility = View.GONE
        view.retry_button.visibility = View.GONE
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
        when(FlowListFragmentArgs.fromBundle(requireArguments()).text){
            "Popis proizvoda" ->{
                inflater.inflate(R.menu.add_menu,menu)
            }
            /*"Popis zaposlenika" -> {
                inflater.inflate(R.menu.add_menu,menu)
            }*/

        }
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
                "Popis zaposlenika" -> {
                    flowListViewModel.apply {
                        employeePopup?.cancel()
                        employeePopup = addEmployeePopup("Insert employee")
                        employeePopup?.setCancelable(false)
                        employeePopup?.setCanceledOnTouchOutside(false)
                        employeePopup?.show()
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
        binding.viewModel = flowListViewModel
        flowListViewModel.edit = item != null
        if(item != null){
            flowListViewModel.nameInput = item.productName.split(":")[0]
            flowListViewModel.priceInput = item.price.toString()
            flowListViewModel.item = item
        }else{
            binding.categoryGroup.visibility = View.VISIBLE
            flowListViewModel.item = null
        }
        flowListViewModel.title = text

        return builder.setView(binding.root).create()
    }

    private fun addEmployeePopup(
        text: String,
        item: EmployeeDTO? = null
    ): AlertDialog? {
        val builder = AlertDialog.Builder(context)
        val binding: EmployeeItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.employee_item, LinearLayout(context), false)
        binding.viewModel = flowListViewModel
        flowListViewModel.editEmployee = item != null
        if(item != null){
            flowListViewModel.firstNameInput = item.employeeName
            flowListViewModel.lastNameInput = item.employeeLastName
            flowListViewModel.address = item.address ?: ""
            flowListViewModel.employee = item
            flowListViewModel.selectedLocation = flowListViewModel.locations.value?.find { it.locationId == item.locationId}
            flowListViewModel.selectedStore = flowListViewModel.allStores.value?.find { it.id == item.storeId}
            binding.location.setText(flowListViewModel.locations.value?.find { it.locationId == item.locationId}?.locationName ?: "")
            binding.store.setText(flowListViewModel.allStores.value?.find { it.id == item.storeId}?.storeName ?: "")

        }else{

            flowListViewModel.employee = null
        }
        binding.store.setOnClickListener {
            storeDialog(binding)
        }
        binding.location.setOnClickListener {
            locationDialog(binding)
        }
        flowListViewModel.employeeTitle = text

        return builder.setView(binding.root).create()
    }

    var storeDialog: AlertDialog? = null
    private fun storeDialog(binding: EmployeeItemBinding) {
        flowListViewModel.getAllStores()
        flowListViewModel.allStores.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.select_store))
            builder.setItems(it.map { itt -> itt.storeName }.toTypedArray()
            ) { dialog, which ->
                flowListViewModel.selectedStore = it[which]
                binding.store.setText(it[which].storeName!!)
                dialog?.dismiss() }
            storeDialog?.cancel()
            storeDialog = builder.create()
            storeDialog?.show()
        })

    }

    var locationDialog: AlertDialog? = null
    private fun locationDialog(binding: EmployeeItemBinding) {
        flowListViewModel.getAllLocations()
        flowListViewModel.locations.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.select_location_resource))
            builder.setItems(it.map { itt -> itt.locationName }.toTypedArray()
            ) { dialog, which ->
                flowListViewModel.selectedLocation = it[which]
                binding.location.setText(it[which].locationName!!)
                dialog?.dismiss() }
            locationDialog?.cancel()
            locationDialog = builder.create()
            locationDialog?.show()
        })

    }


}