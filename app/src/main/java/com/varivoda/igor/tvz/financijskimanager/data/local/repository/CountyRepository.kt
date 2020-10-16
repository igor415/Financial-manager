package com.varivoda.igor.tvz.financijskimanager.data.local.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.County
import com.varivoda.igor.tvz.financijskimanager.ui.list.ListDataPagingSource
import kotlinx.coroutines.flow.Flow

class CountyRepository(private val context: Context) {

    fun getCounties(): Flow<PagingData<County>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ListDataPagingSource(AppDatabase.getInstance(context)) }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 2
    }
}