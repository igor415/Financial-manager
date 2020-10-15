package com.varivoda.igor.tvz.financijskimanager.ui.list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.County
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.CountyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ListViewModel(private val context: Context) : ViewModel(){

    private val repository = CountyRepository(context)

    private var currentQueryValue: String? = null

    private var currentCountyResult: Flow<PagingData<County>>? = null
   //private var currentCountyResult: PagingData<County>? = null

    fun getCountiesStream(): Flow<PagingData<County>> {
        val lastResult = currentCountyResult
        if (lastResult != null) {
            return lastResult
        }
        val newResult: Flow<PagingData<County>> = repository.getCounties()
            .cachedIn(viewModelScope)
        currentCountyResult = newResult
       newResult.map { it.map { println("debug : ${it.countyName ?: ""}") } }
        return newResult
    }

   /* fun getCatalogStream() : Flow<PagingData<County>> = flow {
        viewModelScope.launch(Dispatchers.IO) {
            val lastResult = currentCountyResult
            if (lastResult != null) {
                emit(lastResult)
            }
            val newResult: PagingData<County> = repository.getCounties()
                .cachedIn(viewModelScope)
            currentCountyResult = newResult
            emit (newResult)
        }
    }*/




}