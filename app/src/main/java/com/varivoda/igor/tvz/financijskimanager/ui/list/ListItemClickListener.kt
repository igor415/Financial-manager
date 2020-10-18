package com.varivoda.igor.tvz.financijskimanager.ui.list

class ListItemClickListener (val clickListener: (id: Int) -> Unit){
    fun onClick(id: Int) = clickListener(id)
}