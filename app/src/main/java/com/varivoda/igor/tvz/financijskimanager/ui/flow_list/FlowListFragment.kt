package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_flow_list.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FlowListFragment : Fragment() {

    private val flowListAdapter = FlowListAdapterProducts()
    private val flowListAdapterEmployees = FlowListAdapterEmployees()
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
        when(args.text){
            "Popis proizvoda" ->{
                productsFunction(view)
            }
            "Popis zaposlenika" -> {
                employeesFunction(view)
            }
        }

        return view
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