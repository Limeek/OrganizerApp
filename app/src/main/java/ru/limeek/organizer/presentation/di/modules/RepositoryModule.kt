package ru.limeek.organizer.presentation.di.modules

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.data.daos.EventDao
import ru.limeek.organizer.data.daos.LocationDao
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.data.repository.LocationRepository
import ru.limeek.organizer.data.repository.SharedPrefsRepository

@Module
open class RepositoryModule{
    @Provides
    open fun provideEventRepository(eventDao: EventDao, locationDao: LocationDao): EventRepository{
        return EventRepository(eventDao, locationDao)
    }

    @Provides
    open fun provideLocationRepository(locationDao: LocationDao, eventDao: EventDao): LocationRepository{
        return LocationRepository(locationDao, eventDao)
    }

    @Provides
    open fun provideSharedPrefsRepo(sharedPreferences: SharedPreferences): SharedPrefsRepository{
        return SharedPrefsRepository(sharedPreferences)
    }
}