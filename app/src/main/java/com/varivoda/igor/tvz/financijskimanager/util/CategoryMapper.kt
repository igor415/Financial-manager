package com.varivoda.igor.tvz.financijskimanager.util

import com.varivoda.igor.tvz.financijskimanager.R

fun getIdForRadioButton(id: Int): Int =
    when(id){
        R.id.first -> 1
        R.id.second -> 2
        R.id.third -> 3
        R.id.fourth -> 4
        R.id.fifth -> 5
        R.id.sixth -> 6
        else -> 1
    }