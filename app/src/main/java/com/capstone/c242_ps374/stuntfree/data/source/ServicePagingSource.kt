package com.capstone.c242_ps374.stuntfree.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.capstone.c242_ps374.stuntfree.data.api.ApiService

//class ServicePagingSource(
//    private val apiService: ApiService,
//    private val token: String,
//) : PagingSource<Int, Service>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Service> {
//        return try {
//            val currentPage = params.key ?: 1
//            val response = apiService.getAllDokter(
//                token = token,
//                page = currentPage,
//                size = params.loadSize,
//            )
//
//            if (response.isSuccessful) {
//                val responseBody = response.body()
//                val data = responseBody?.listStory ?: emptyList()
//                LoadResult.Page(
//                    data = data,
//                    prevKey = if (currentPage == 1) null else currentPage - 1,
//                    nextKey = if (data.isEmpty()) null else currentPage + 1
//                )
//            } else {
//                LoadResult.Error(Exception("Error: ${response.message()}"))
//            }
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, Service>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
//                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
//        }
//    }
//}
