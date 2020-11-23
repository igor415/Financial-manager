package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.bumptech.glide.load.HttpException
import com.varivoda.igor.tvz.financijskimanager.data.local.dao.ProductDao
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.ProductRepository
import java.io.IOException

const val PAGE_INDEX = 1
/*class ProductPagingSource(private val productRepository: ProductRepository) : PagingSource<Int,Product>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val position = params.key ?: PAGE_INDEX
        return try {
            val response = productRepository.getProductStream()//service.searchRepos(apiQuery, position, params.loadSize)
            LoadResult.Page(
                data = response,
                prevKey = if (position == PAGE_INDEX) null else position - 1,
                nextKey = if (response.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

}*/