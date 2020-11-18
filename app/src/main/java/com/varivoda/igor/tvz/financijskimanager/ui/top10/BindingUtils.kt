package com.varivoda.igor.tvz.financijskimanager.ui.top10

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.varivoda.igor.tvz.financijskimanager.model.ProductDTO

@BindingAdapter("productNameValue")
fun TextView.setProductName(item: ProductDTO) {
    text = item.productName
}

@BindingAdapter("productQuantityValue")
fun TextView.setProductQuantity(item: ProductDTO) {
    text = "${item.quantity}x"
}
