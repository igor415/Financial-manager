package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_flow_list.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FlowListFragment : Fragment() {

    private val flowListAdapter = FlowListAdapter()
    private lateinit var flowListViewModel: FlowListViewModel
    private lateinit var flowListViewModelFactory: FlowListViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_flow_list, container, false)
        val args = FlowListFragmentArgs.fromBundle(requireArguments())
        (activity as HomeActivity).setActionBarText(args.text)
        view.flowListRecyclerView.apply {
            setHasFixedSize(true)
            adapter = flowListAdapter
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flowListViewModelFactory = FlowListViewModelFactory(requireContext())
        flowListViewModel = ViewModelProvider(this,flowListViewModelFactory).get(FlowListViewModel::class.java)
        flowListViewModel.allProducts.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            flowListAdapter.submitList(it)
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