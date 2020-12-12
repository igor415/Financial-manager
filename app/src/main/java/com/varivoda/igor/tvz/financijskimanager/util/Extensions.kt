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
import java.security.MessageDigest
import kotlin.experimental.and

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

fun String.toSHA1(): String? {
    return try {
        val digest = MessageDigest.getInstance("SHA-1")
        val textBytes = this.toByteArray(charset("iso-8859-1"))
        digest.update(textBytes, 0, textBytes.size)
        val sha1hash = digest.digest()
        convertToHex(sha1hash)
    } catch (e: Exception) {
        null
    }
}

private fun convertToHex(data: ByteArray): String? {
    val buf = java.lang.StringBuilder()
    for (b in data) {
        var halfbyte: Int = b.toInt() shr 4 and 0x0F
        var two_halfs = 0
        do {
            buf.append(if (halfbyte in 0..9) ('0'.toInt() + halfbyte).toChar() else ('a'.toInt() + (halfbyte - 10)).toChar())
            halfbyte = (b and 0x0F).toInt()
        } while (two_halfs++ < 1)
    }
    return buf.toString()
}