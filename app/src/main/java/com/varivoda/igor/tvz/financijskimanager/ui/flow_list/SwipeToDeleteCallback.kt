package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeToDeleteCallback(private val adapterCustomers: FlowListAdapterCustomers? = null,
                            private val adapterEmployees: FlowListAdapterEmployees? = null,
                            private val adapterProducts: FlowListAdapterProducts? = null,
                            private val flowListViewModel: FlowListViewModel) : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if(adapterCustomers != null){
            adapterCustomers.deleteCustomer(viewHolder.adapterPosition)
        }else if(adapterProducts != null){
            adapterProducts.deleteProduct(viewHolder.adapterPosition)
        }else{
            adapterEmployees?.deleteEmployee(viewHolder.adapterPosition)
        }
        println("debug this is ${viewHolder.adapterPosition} ${direction}  ")
    }


}