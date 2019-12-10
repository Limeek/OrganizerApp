package ru.limeek.organizer.presentation.di.modules

import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.data.daos.EventDao
import ru.limeek.organizer.data.daos.LocationDao
import ru.limeek.organizer.data.database.AppDatabase
import ru.limeek.organizer.presentation.app.App
import javax.inject.Singleton

@Module
open class RoomModule {

    @Singleton
    @Provides
    open fun providesDb(app: App): AppDatabase {
        return Room.databaseBuilder(app.applicationContext, AppDatabase::class.java, "OrganizerDB")
                .fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    open fun providesEventDao(appDb: AppDatabase): EventDao {
        return appDb.eventDao()
    }

    @Singleton
    @Provides
    open fun providesLocationDao(appDb: AppDatabase): LocationDao {
        return appDb.locationDao()
    }

}