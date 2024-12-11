package com.capstone.c242_ps374.stuntfree.data.news

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @field:SerializedName("status") val status: String?,
    @field:SerializedName("totalResults") val totalResults: Int?,
    @field:SerializedName("articles") val articles: List<Article?>?
)