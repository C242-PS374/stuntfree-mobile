package com.capstone.c242_ps374.stuntfree.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.c242_ps374.stuntfree.data.model.DayItem
import com.capstone.c242_ps374.stuntfree.data.news.Article
import com.capstone.c242_ps374.stuntfree.data.news.NewsResponse
import com.capstone.c242_ps374.stuntfree.data.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val serviceRepository: ArticleRepository
) : ViewModel() {

    private val _news = MutableLiveData<Result<NewsResponse>>()
    val news: LiveData<Result<NewsResponse>> get() = _news

    private val _buttonServices = MutableLiveData<List<Article>>()
    val buttonServices: LiveData<List<Article>> get() = _buttonServices

    private val _weekDays = MutableLiveData<List<DayItem>>()
    val weekDays: LiveData<List<DayItem>> get() = _weekDays

    private val _todayDate = MutableLiveData<String>()
    val todayDate: LiveData<String> get() = _todayDate

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var allArticles: List<Article> = listOf()
    private var lastClickedAuthor: String? = null

    fun fetchNews() {
        _isLoading.postValue(true)
        _error.postValue(null)
        viewModelScope.launch {
            val result = serviceRepository.getNews()
            result.onSuccess { response ->
                allArticles = (response.articles ?: listOf()) as List<Article>
                _news.postValue(Result.success(response))
            }.onFailure { exception ->
                _error.postValue(exception.message)
            }.also {
                _isLoading.postValue(false)
            }
        }
    }

    // Filter articles by author
    fun filterNewsByAuthor(authorName: String) {
        if (lastClickedAuthor == authorName) {
            lastClickedAuthor = null
            _news.postValue(Result.success(NewsResponse(
                status = "ok",
                totalResults = allArticles.size,
                articles = allArticles
            )))
        } else {
            lastClickedAuthor = authorName
            val filteredArticles = allArticles.filter { it.author == authorName }
            _news.postValue(Result.success(NewsResponse(
                status = "ok",
                totalResults = filteredArticles.size,
                articles = filteredArticles
            )))
        }
    }

    fun searchArticles(query: String) {
        viewModelScope.launch {
            val filteredArticles = withContext(Dispatchers.Default) {
                allArticles.filter {
                    it.title?.contains(query, ignoreCase = true) == true ||
                            it.description?.contains(query, ignoreCase = true) == true
                }
            }
            _news.postValue(Result.success(NewsResponse(
                status = "ok",
                totalResults = filteredArticles.size,
                articles = filteredArticles
            )))
        }
    }

    fun generateWeekDays() {
        _weekDays.value = getWeekDays()
    }

    private fun getWeekDays(): List<DayItem> {
        val weekDaysList = mutableListOf<DayItem>()
        val calendar = Calendar.getInstance()

        for (i in 0..31) {
            val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)
            val dayName = SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.time)
            val isSelected = (i == 0)
            weekDaysList.add(DayItem(date, dayName, isSelected))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return weekDaysList
    }
}