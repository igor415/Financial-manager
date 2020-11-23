package com.varivoda.igor.tvz.financijskimanager.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController

class HomeViewModelFactory(private val navController: NavController) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(navController) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}