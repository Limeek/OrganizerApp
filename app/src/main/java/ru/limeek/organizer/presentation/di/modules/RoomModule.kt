package ru.limeek.organizer.presentation.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.data.daos.EventDao
import ru.limeek.organizer.data.daos.LocationDao
import ru.limeek.organizer.data.database.AppDatabase
import ru.limeek.organizer.presentation.di.scopes.AppScope
import javax.inject.Singleton

@Module
open class RoomModule(var appContext: Context) {
    private var appDb: AppDatabase = Room.databaseBuilder(appContext, AppDatabase::class.java, "OrganizerDB")
            .fallbackToDestructiveMigration()
            .build()

    @AppScope
    @Provides
    open fun providesDb(): AppDatabase {
        return appDb
    }

    @AppScope
    @Provides
    open fun providesEventDao(): EventDao {
        return appDb.eventDao()
    }

    @AppScope
    @Provides
    open fun providesLocationDao(): LocationDao {
        return appDb.locationDao()
    }

}