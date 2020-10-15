package com.varivoda.igor.tvz.financijskimanager.ui.list

import androidx.paging.PagingSource
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.County
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class ListDataPagingSource(private val database: AppDatabase): PagingSource<Int,County>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, County> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            GlobalScope.async(Dispatchers.IO) {
                val response = database.countyDao.getCounties()
                delay(3500)
                LoadResult.Page(
                    data = response,
                    prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                    nextKey = if (response.isEmpty()) null else position + 1
                )
            }.await()
        }catch (exception: IOException) {
            return LoadResult.Error(exception)
        }

    }

}