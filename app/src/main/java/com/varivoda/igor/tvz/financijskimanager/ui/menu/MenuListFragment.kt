package com.varivoda.igor.tvz.financijskimanager.ui.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.ui.selector.FragmentSelector
import com.varivoda.igor.tvz.financijskimanager.util.toast
import kotlinx.android.synthetic.main.menu_list_fragment.view.*

class MenuListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.menu_list_fragment, container, false)
        val args = MenuListFragmentArgs.fromBundle(requireArguments())
        setActionBarTextAndItems(args.item,view)
        return view
    }


    private fun setActionBarTextAndItems(item: String, view: View) {
        when(item){
            Menu.PRODUCTS.string -> {
                (activity as HomeActivity).setActionBarText(getString(R.string.products_title))
                setRecyclerViewData(view, resources.getStringArray(R.array.productsOptions).toList())
            }
            Menu.CUSTOMERS.string -> {
                (activity as HomeActivity).setActionBarText(getString(R.string.customers_title))
                setRecyclerViewData(view, resources.getStringArray(R.array.customersOptions).toList())
            }
            Menu.INSERT_BILL.string -> {
                (activity as HomeActivity).setActionBarText(getString(R.string.insert_bill_title))
                setRecyclerViewData(view, resources.getStringArray(R.array.billsOptions).toList())
            }
            Menu.EMPLOYEES.string -> {
                (activity as HomeActivity).setActionBarText(getString(R.string.employees_title))
                setRecyclerViewData(view, resources.getStringArray(R.array.employeesOptions).toList())
            }
            Menu.STORES.string -> {
                (activity as HomeActivity).setActionBarText(getString(R.string.stores_title))
                setRecyclerViewData(view, resources.getStringArray(R.array.storesOptions).toList())
            }
            Menu.STATISTICS.string -> {
                (activity as HomeActivity).setActionBarText(getString(R.string.statistics_title))
                setRecyclerViewData(view, resources.getStringArray(R.array.statisticsOptions).toList())
            }
        }
    }

    private fun setRecyclerViewData(view: View,list: List<String>){
        val menuAdapter = MenuListAdapter(MenuItemClickListener { it -> context?.toast(it)
        FragmentSelector().navigate(it,findNavController())
        })
        menuAdapter.submitList(list)
        view.menuListRecyclerView.apply {
            setHasFixedSize(true)
            adapter = menuAdapter
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }


}