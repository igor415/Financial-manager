package com.varivoda.igor.tvz.financijskimanager.ui.home

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.varivoda.igor.tvz.financijskimanager.ui.menu.Menu

class HomeViewModel(private val navController: NavController) : ViewModel(){

    fun onStatisticsClick() = navController.navigate(HomeFragmentDirections.actionHomeFragmentToMenuListFragment(
        Menu.STATISTICS.string))

    fun onCustomersClicked() = navController.navigate(HomeFragmentDirections.actionHomeFragmentToMenuListFragment(Menu.CUSTOMERS.string))

    fun onEmployeeClicked() = navController.navigate(HomeFragmentDirections.actionHomeFragmentToMenuListFragment(Menu.EMPLOYEES.string))

    fun onInvoiceClicked() = navController.navigate(HomeFragmentDirections.actionHomeFragmentToMenuListFragment(Menu.INSERT_BILL.string))

    fun onProductsClicked() = navController.navigate(HomeFragmentDirections.actionHomeFragmentToMenuListFragment(Menu.PRODUCTS.string))

    fun onStoresClicked() = navController.navigate(HomeFragmentDirections.actionHomeFragmentToMenuListFragment(Menu.STORES.string))
}