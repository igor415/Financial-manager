package com.varivoda.igor.tvz.financijskimanager.ui.picture

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.util.convertStringToBitmap

class PictureAdapter : ListAdapter<Product,PictureAdapter.PictureViewHolder>(PictureDiffCallback()){

    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        return PictureViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                listener?.onClick(getItem(position))
            }
        }
    }

    class PictureViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val pictureTextView = itemView.findViewById<TextView>(R.id.pictureTextView)
        private val pictureImage = itemView.findViewById<ImageView>(R.id.pictureImage)

        fun bind(product: Product) {
            pictureTextView.text = product.productName
            if (product.image != null) {
                pictureImage.setImageBitmap(product.image!!.convertStringToBitmap())
            } else {
                pictureImage.setImageResource(R.drawable.product_picture_placeholder)
            }
        }

        companion object{
            fun create(parent: ViewGroup): PictureViewHolder{
                return PictureViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.picture_item,parent,false))
            }
        }
    }

    class PictureDiffCallback : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id && oldItem.image == newItem.image
        }

    }

    interface OnItemClickListener {
        fun onClick(product: Product)
    }

    fun setOnItemClickListener(lis: OnItemClickListener) {
        listener = lis
    }

}