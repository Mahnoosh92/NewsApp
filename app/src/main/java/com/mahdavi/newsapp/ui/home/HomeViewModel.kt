package com.mahdavi.newsapp.ui.home

import androidx.lifecycle.*
import com.mahdavi.newsapp.data.model.remote.Article
import com.mahdavi.newsapp.data.model.remote.News
import com.mahdavi.newsapp.data.repository.NewsRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val newsRepository: NewsRepository) : ViewModel() {
    private var _articles: MutableLiveData<List<Article>> = MutableLiveData<List<Article>>()
    private val articles: LiveData<List<Article>>
        get() = _articles

    fun getNews(topic: String = "bitcoin") {
        viewModelScope.launch {
            val result = newsRepository.getNews(topic)
            when(result) {

                is com.mahdavi.newsapp.data.model.local.Result.Error -> {}
                is com.mahdavi.newsapp.data.model.local.Result.Value -> {

                }
                else -> throw IllegalArgumentException("Undefined type")
            }
        }

    }

}

class HomeViewModelFactory(private val newsRepository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModelFactory::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModelFactory(newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}