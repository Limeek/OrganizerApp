package ru.limeek.organizer.di.modules

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.limeek.organizer.BuildConfig
import ru.limeek.organizer.api.DarkSkyApi
import ru.limeek.organizer.api.NewsApi
import ru.limeek.organizer.di.scopes.AppScope

@Module
class RetrofitModule {

    @AppScope
    @Provides
    fun providesDarkSkyApi() : DarkSkyApi {
        return Retrofit.Builder()
                .baseUrl("https://api.darksky.net/forecast/${BuildConfig.DarkSkyApiKey}/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(DarkSkyApi::class.java)
    }

    @AppScope
    @Provides
    fun providesNewsApi() : NewsApi {
        return Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(NewsApi::class.java)
    }
}