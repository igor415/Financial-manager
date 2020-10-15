package com.varivoda.igor.tvz.financijskimanager.ui.selector

import androidx.navigation.NavController
import com.varivoda.igor.tvz.financijskimanager.R

class FragmentSelector() {

    fun navigate(text: String,navController: NavController){
        when(text){
            "Popis kupaca" ->{
                navController.navigate(R.id.action_menuListFragment_to_listFragment)
            }
        }
    }
}