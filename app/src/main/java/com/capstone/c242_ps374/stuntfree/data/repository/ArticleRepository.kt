package com.capstone.c242_ps374.stuntfree.data.repository

import com.capstone.c242_ps374.stuntfree.data.api.NewsApiService
import com.capstone.c242_ps374.stuntfree.data.news.Article
import com.capstone.c242_ps374.stuntfree.data.news.NewsResponse
import javax.inject.Inject
import javax.inject.Named

class ArticleRepository @Inject constructor(
    @Named("newsApiService") private val apiService: NewsApiService
) {
    suspend fun getNews(): Result<NewsResponse> {
        return try {
            val response = apiService.getNews()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                val error = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("API Error: $error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}