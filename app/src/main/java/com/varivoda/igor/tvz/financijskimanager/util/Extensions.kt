package com.varivoda.igor.tvz.financijskimanager.util

import android.app.NotificationManager
import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.model.ProductDTO

fun Context?.toast(text: String){
    Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
}

fun View.getSnackBar(text: String): Snackbar{
    return Snackbar.make(this,text,Snackbar.LENGTH_LONG)
}

fun Context.styleableToast(text: String, styleId: Int) {
    com.muddzdev.styleabletoast.StyleableToast.makeText(this, text, Toast.LENGTH_LONG, styleId)
        .show()
}

fun List<Product>.asDomainModel(): List<ProductDTO> {
    return map {
        ProductDTO(
            productName = it.productName,
            quantity = it.price)
    }
}