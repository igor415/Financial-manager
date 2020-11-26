package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.ui.flow_list.FlowListViewModel.ProductModel
import com.varivoda.igor.tvz.financijskimanager.ui.flow_list.separator.SeparatorViewHolder

class FlowListAdapterProducts(private val openPopup: (Product) -> Unit,
                              private val viewModel: FlowListViewModel) : PagingDataAdapter<ProductModel, RecyclerView.ViewHolder>(
    UIMODEL_COMPARATOR){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == R.layout.separator_view_item){
            SeparatorViewHolder.create(parent)
        }else{
            FlowListViewHolder.create(parent)
        }
    }


    override fun getItemViewType(position: Int): Int {
        println("debug ${getItem(position)}")
        return when (getItem(position)) {
            is ProductModel.ProductItem -> R.layout.menu_list_item
            is ProductModel.SeparatorItem -> R.layout.separator_view_item
            null -> throw UnsupportedOperationException("Unknown view")//R.layout.separator_view_item
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = getItem(position)
        model.let {
            when (model) {
                is ProductModel.ProductItem -> {
                    (holder as FlowListViewHolder).bind(model.product)
                    holder.itemView.setOnLongClickListener{
                        openPopup.invoke(model.product)
                        true
                    }
                }
                is ProductModel.SeparatorItem -> (holder as SeparatorViewHolder).bind(model.description)
            }
        }
        //getItem(position)?.let { holder.bind(it) }
       /* holder.itemView.setOnLongClickListener {
            //getItem(position)?.let { it1 -> openPopup.invoke(it1) }
            true
        }*/
    }


   /* class DiffCallback : DiffUtil.ItemCallback<ProductModel>() {
        override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
            return (oldItem is ProductModel.ProductItem && newItem is ProductModel.ProductItem && oldItem.product.id == newItem.product.id)
                    || (oldItem is ProductModel.SeparatorItem && newItem is ProductModel.SeparatorItem
                    && oldItem.description == newItem.description)
        }

        override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
            return oldItem == newItem
        }

    }*/

    companion object {
        private val UIMODEL_COMPARATOR = object : DiffUtil.ItemCallback<ProductModel>() {
            override fun areItemsTheSame(
                oldItem: ProductModel,
                newItem: ProductModel
            ): Boolean {
                return (oldItem is ProductModel.ProductItem && newItem is ProductModel.ProductItem &&
                        oldItem.product.id == newItem.product.id) ||
                        (oldItem is ProductModel.SeparatorItem && newItem is ProductModel.SeparatorItem &&
                                oldItem.description == newItem.description)
            }

            override fun areContentsTheSame(
                oldItem: ProductModel,
                newItem: ProductModel
            ): Boolean {
                return oldItem == newItem
            }/*
            override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
                return (oldItem is ProductModel.ProductItem && newItem is ProductModel.ProductItem &&
                        oldItem.product.id == newItem.product.id) ||
                        (oldItem is ProductModel.SeparatorItem && newItem is ProductModel.SeparatorItem &&
                                oldItem.description == newItem.description)
            }

            override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
                return oldItem == newItem
            }*/
        }
    }

    fun deleteProduct(position: Int){
        val model = getItem(position)
        if(model is ProductModel.ProductItem){
            viewModel.deleteProduct(model.product.id)
        }
    }

}