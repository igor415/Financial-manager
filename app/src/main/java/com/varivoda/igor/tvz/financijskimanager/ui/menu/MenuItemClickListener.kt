package com.varivoda.igor.tvz.financijskimanager.ui.menu

class MenuItemClickListener (val clickListener: (name: String) -> Unit){
    fun onClick(item: String) = clickListener(item)
}