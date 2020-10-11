package com.varivoda.igor.tvz.financijskimanager.util

import android.app.NotificationManager
import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun Context?.toast(text: String){
    Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
}

fun View.getSnackBar(text: String): Snackbar{
    return Snackbar.make(this,text,Snackbar.LENGTH_LONG)
}
