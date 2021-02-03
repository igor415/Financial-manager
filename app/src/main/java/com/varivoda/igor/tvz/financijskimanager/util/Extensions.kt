package com.varivoda.igor.tvz.financijskimanager.util

import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.model.*
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

fun List<DataOnBill>.toReturnDataList(): List<ReturnData>{
    return map{
        ReturnData(it.id ?: 0, 1, it.quantity?: 0)
    }
}

fun List<ProductStockDTO>.toDomainModel(): List<InventoryDTO> {
    return map {
        InventoryDTO(it.productId, it.quantity)
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

fun String.toSha2(): String {
    val HEX_CHARS = "0123456789ABCDEF"
    val bytes = MessageDigest
        .getInstance("SHA-256")
        .digest(this.toByteArray())
    val result = StringBuilder(bytes.size * 2)

    bytes.forEach {
        val i = it.toInt()
        result.append(HEX_CHARS[i shr 4 and 0x0f])
        result.append(HEX_CHARS[i and 0x0f])
    }

    return result.toString()
}

fun EditText.openKeyboard(context: Context?){
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun EditText.closeKeyboard(context: Context?){
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken,0)
}

