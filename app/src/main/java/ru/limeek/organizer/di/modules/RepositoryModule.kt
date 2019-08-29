package ru.limeek.organizer.di.modules

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.data.model.event.EventDao
import ru.limeek.organizer.data.model.location.LocationDao
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.data.repository.LocationRepository
import ru.limeek.organizer.data.repository.SharedPrefsRepository
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
    fun provideSharedPrefsRepo(sharedPreferences: SharedPreferences): SharedPrefsRepository{
        return SharedPrefsRepository(sharedPreferences)
    }
}