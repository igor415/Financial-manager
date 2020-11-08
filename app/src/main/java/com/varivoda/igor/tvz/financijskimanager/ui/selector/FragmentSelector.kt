package com.varivoda.igor.tvz.financijskimanager.ui.selector

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.ui.maps.MapsActivity
import com.varivoda.igor.tvz.financijskimanager.ui.menu.MenuListFragmentDirections

class FragmentSelector() {

    fun navigate(text: String,navController: NavController,activity: FragmentActivity?){
        when(text){
            /*"Popis kupaca" ->{
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToListFragment("Popis kupaca"))
                //navController.navigate(R.id.action_menuListFragment_to_listFragment)
            }*/
            "Popis proizvoda" -> {
                //navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToListFragment("Popis proizvoda"))
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToFlowListFragment("Popis proizvoda"))
            }
            "Popis zaposlenika" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToFlowListFragment("Popis zaposlenika"))
            }
            "Popis kupaca" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToFlowListFragment("Popis kupaca"))
            }
            "Popis poslovnica" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToFlowListFragment("Popis poslovnica"))
            }
            "Lokacije poslovnica" -> {
                activity?.startActivity(Intent(activity, MapsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY))
            }
            "Popis računa" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToBillFragment("Popis računa"))
            }
            "Zaposlenik koji je uprihodio najveću svotu novca po mjesecu" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToBillFragment("Zaposlenik koji je uprihodio najveću svotu novca po mjesecu"))
            }
            "Proizvod koji se najbolje prodaje po kvartalima godine" ->{
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToQuarterFragment("Proizvod koji se najbolje prodaje po kvartalima godine"))
            }
        }
    }
}