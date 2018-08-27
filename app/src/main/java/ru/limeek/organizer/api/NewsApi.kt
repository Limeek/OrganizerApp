package ru.limeek.organizer.api

import io.reactivex.Flowable
import retrofit2.http.GET
import ru.limeek.organizer.BuildConfig
import ru.limeek.organizer.model.news.News

interface NewsApi {
    @GET("v2/top-headlines?country=ru&apiKey=${BuildConfig.NewsApiKey}")
    fun getRecentNews() : Flowable<News>
}