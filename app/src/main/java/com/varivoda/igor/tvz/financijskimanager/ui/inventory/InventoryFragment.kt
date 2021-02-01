package com.varivoda.igor.tvz.financijskimanager.ui.inventory

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.varivoda.igor.tvz.financijskimanager.App
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_inventory.*
import kotlinx.android.synthetic.main.fragment_inventory.view.*

class InventoryFragment : Fragment() {

    private val inventoryAdapter = InventoryAdapter()
    private val inventoryViewModel by viewModels<InventoryViewModel> {
        InventoryViewModelFactory((requireContext().applicationContext as App).inventoryRepository,
            (requireContext().applicationContext as App).storeRepository,
            (requireContext().applicationContext as App).productRepository,
            (requireContext().applicationContext as App).preferences)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_inventory, container, false)
        view.inventoryRecyclerView.adapter = inventoryAdapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = InventoryFragmentArgs.fromBundle(requireArguments())
        (activity as HomeActivity).setActionBarText(args.text)

        addInventory.setOnClickListener {
            findNavController().navigate(InventoryFragmentDirections.actionInventoryFragmentToCheckInventoryFragment())
        }
        observeInventoryItems()
    }

    private fun observeInventoryItems() {
        inventoryViewModel.inventoryItems.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            inventoryAdapter.submitList(it)
        })
    }

    override fun onResume() {
        super.onResume()
        inventoryViewModel.getInventoryItems()
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