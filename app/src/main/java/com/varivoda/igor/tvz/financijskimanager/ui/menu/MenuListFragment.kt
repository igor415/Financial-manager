package com.varivoda.igor.tvz.financijskimanager.ui.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
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
                (activity as HomeActivity).setActionBarText("Products")
                setRecyclerViewData(view, resources.getStringArray(R.array.productsOptions).toList())
            }
            Menu.CUSTOMERS.string -> {
                (activity as HomeActivity).setActionBarText("Customers")
                setRecyclerViewData(view, resources.getStringArray(R.array.customersOptions).toList())
            }
            Menu.INSERT_BILL.string -> {
                (activity as HomeActivity).setActionBarText("Insert Bill")
                setRecyclerViewData(view, resources.getStringArray(R.array.billsOptions).toList())
            }
            Menu.EMPLOYEES.string -> {
                (activity as HomeActivity).setActionBarText("Employees")
                setRecyclerViewData(view, resources.getStringArray(R.array.employeesOptions).toList())
            }
            Menu.STORES.string -> {
                (activity as HomeActivity).setActionBarText("Stores")
                setRecyclerViewData(view, resources.getStringArray(R.array.storesOptions).toList())
            }
            Menu.STATISTICS.string -> {
                (activity as HomeActivity).setActionBarText("Statistics")
                setRecyclerViewData(view, resources.getStringArray(R.array.statisticsOptions).toList())
            }
        }
    }

    private fun setRecyclerViewData(view: View,list: List<String>){
        view.menuListRecyclerView.apply {
            setHasFixedSize(true)
            adapter = MenuListAdapter(list)
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