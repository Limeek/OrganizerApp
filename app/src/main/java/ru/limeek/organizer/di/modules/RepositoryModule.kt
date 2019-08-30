package ru.limeek.organizer.di.modules

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.data.model.event.EventDao
import ru.limeek.organizer.data.model.location.LocationDao
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.data.repository.LocationRepository
import ru.limeek.organizer.data.repository.SharedPrefsRepository

@Module
class RepositoryModule{
    @Provides
    fun provideEventRepository(eventDao: EventDao, locationDao: LocationDao): EventRepository{
        return EventRepository(eventDao, locationDao)
    }

    @Provides
    fun provideLocationRepository(locationDao: LocationDao, eventDao: EventDao): LocationRepository{
        return LocationRepository(locationDao, eventDao)
    }

    @Provides
    fun provideSharedPrefsRepo(sharedPreferences: SharedPreferences): SharedPrefsRepository{
        return SharedPrefsRepository(sharedPreferences)
    }
}