package ru.limeek.organizer.repository

import io.reactivex.Flowable
import ru.limeek.organizer.api.NewsApi
import ru.limeek.organizer.model.news.News
import javax.inject.Inject

class NewsRepository @Inject constructor(var newsApi: NewsApi) {
    fun getRecentNews(): Flowable<News> {
        return newsApi.getRecentNews()
    }
}