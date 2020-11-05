package com.varivoda.igor.tvz.financijskimanager.model

data class BillDTO(
    var id: Int,
    var money: Double,
    var date: String,
    var storeName: String
)