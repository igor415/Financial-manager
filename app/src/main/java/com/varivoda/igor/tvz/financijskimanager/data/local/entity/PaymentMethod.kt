package com.varivoda.igor.tvz.financijskimanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PaymentMethod (
    @PrimaryKey
    var id: Int,
    var name: String
)

