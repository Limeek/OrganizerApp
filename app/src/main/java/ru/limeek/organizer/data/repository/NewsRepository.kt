package ru.limeek.organizer.data.repository

import io.reactivex.Flowable
import ru.limeek.organizer.data.api.NewsApi
import ru.limeek.organizer.data.model.news.News
import javax.inject.Inject

class NewsRepository @Inject constructor(var newsApi: NewsApi) {
    fun getRecentNews(): Flowable<News> {
        return newsApi.getRecentNews()
    }
}