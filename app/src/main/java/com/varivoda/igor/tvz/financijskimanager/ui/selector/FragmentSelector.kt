package com.varivoda.igor.tvz.financijskimanager.ui.selector

import androidx.navigation.NavController
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.menu.MenuListFragmentDirections

class FragmentSelector() {

    fun navigate(text: String,navController: NavController){
        when(text){
            "Popis kupaca" ->{
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToListFragment("Popis kupaca"))
                //navController.navigate(R.id.action_menuListFragment_to_listFragment)
            }
            "Popis proizvoda" -> {
                //navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToListFragment("Popis proizvoda"))
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToFlowListFragment("Popis proizvoda"))
            }
            "Popis zaposlenika" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToFlowListFragment("Popis zaposlenika"))
            }
        }
    }
}