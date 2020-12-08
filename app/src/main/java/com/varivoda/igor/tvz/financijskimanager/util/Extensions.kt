package com.varivoda.igor.tvz.financijskimanager.util

import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.model.ProductDTO
import java.io.ByteArrayOutputStream

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

fun String.convertStringToBitmap(): Bitmap? {
    val decodedString: ByteArray = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
}

fun Bitmap.bitmapToBase64(): String? {
    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val b = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}