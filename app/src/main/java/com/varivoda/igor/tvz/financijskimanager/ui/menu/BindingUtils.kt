package com.varivoda.igor.tvz.financijskimanager.ui.menu

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("Name")
fun TextView.setName(text: String){
    this.text = text
}