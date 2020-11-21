package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.varivoda.igor.tvz.financijskimanager.R

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        /*val imgUri =
            imgUrl.toUri().buildUpon().scheme("https").build()*/
        Glide.with(imgView.context)
            .load(imgUrl)
            .into(imgView)
    }
}