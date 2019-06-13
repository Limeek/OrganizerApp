package ru.limeek.organizer.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.limeek.organizer.data.api.NewsApi
import ru.limeek.organizer.data.model.news.News
import javax.inject.Inject

class NewsRepository @Inject constructor(var newsApi: NewsApi) {
    suspend fun getRecentNews(): News {
        return withContext(Dispatchers.IO){
            newsApi.getRecentNews()
        }
    }
}