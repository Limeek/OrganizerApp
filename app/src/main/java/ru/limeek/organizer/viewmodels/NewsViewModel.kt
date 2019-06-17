package ru.limeek.organizer.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.limeek.organizer.data.model.news.News
import ru.limeek.organizer.data.repository.NewsRepository
import kotlin.coroutines.CoroutineContext

class NewsViewModel(var newsRepo: NewsRepository): ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.IO + SupervisorJob()
    var news = MutableLiveData<News>()

    init {
        getNews()
    }

    private fun getNews() {
        launch {
            news.postValue(newsRepo.getRecentNews())
        }
    }
}