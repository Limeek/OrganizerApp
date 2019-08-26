package ru.limeek.organizer.di.modules

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.data.api.DarkSkyApi
import ru.limeek.organizer.data.api.NewsApi
import ru.limeek.organizer.data.model.event.EventDao
import ru.limeek.organizer.data.model.location.LocationDao
import ru.limeek.organizer.data.repository.*
import ru.limeek.organizer.di.scopes.ViewModelScope

@Module
class RepositoryModule{
    @Provides
    @ViewModelScope
    fun provideEventRepository(eventDao: EventDao, locationDao: LocationDao): EventRepository{
        return EventRepository(eventDao, locationDao)
    }

    @Provides
    @ViewModelScope
    fun provideLocationRepository(locationDao: LocationDao, eventDao: EventDao): LocationRepository{
        return LocationRepository(locationDao, eventDao)
    }

    @Provides
    @ViewModelScope
    fun provideWeatherRepository(darkSkyApi: DarkSkyApi): WeatherRepository{
        return WeatherRepository(darkSkyApi)
    }

    @Provides
    @ViewModelScope
    fun provideNewsRepository(newsApi: NewsApi): NewsRepository{
        return NewsRepository(newsApi)
    }

    @Provides
    @ViewModelScope
    fun provideSharedPrefsRepo(sharedPreferences: SharedPreferences): SharedPrefsRepository{
        return SharedPrefsRepository(sharedPreferences)
    }
}