package com.capstone.c242_ps374.stuntfree.data.api

import com.capstone.c242_ps374.stuntfree.data.news.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("everything")
    suspend fun getNews(
        @Query("q") query: String = "pregnancy",
        @Query("apiKey") apiKey: String = "e07f25cfc2044009b0cbfb5387c61951",
        @Query("language") language: String = "en",
        @Query("sortBy") sortBy: String = "publishedAt"
    ): Response<NewsResponse>
}