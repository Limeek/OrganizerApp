package ru.limeek.organizer.di.modules

import android.arch.persistence.room.Room
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.app.App
import ru.limeek.organizer.database.AppDatabase
import ru.limeek.organizer.di.scopes.AppScope
import ru.limeek.organizer.model.Event.EventDao
import ru.limeek.organizer.model.Location.LocationDao

@Module
class RoomModule(var app: App) {
    private var appDb : AppDatabase = Room.databaseBuilder(app,AppDatabase::class.java,"OrganizerDB")
            .addMigrations(AppDatabase.MIGRATION_2_3,AppDatabase.MIGRATION_3_4, AppDatabase.MIGRATION_4_5, AppDatabase.MIGRATION_5_6)
            .build()
    @AppScope
    @Provides
    fun providesDb() : AppDatabase{
        return appDb
    }

    @AppScope
    @Provides
    fun providesEventDao() : EventDao {
        return appDb.eventDao()
    }

    @AppScope
    @Provides
    fun providesLocationDao() : LocationDao{
        return appDb.locationDao()
    }



}