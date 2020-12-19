package com.varivoda.igor.tvz.financijskimanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey
    val id: Int,
    val name: String
)