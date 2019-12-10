package ru.limeek.organizer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.limeek.organizer.data.daos.EventDao
import ru.limeek.organizer.data.daos.LocationDao
import ru.limeek.organizer.domain.entities.event.Event
import ru.limeek.organizer.domain.entities.location.Location

@Database(entities = [Event::class, Location::class], version = 1,exportSchema = false)
@TypeConverters(DateTimeConverter::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun eventDao() : EventDao
    abstract fun locationDao() : LocationDao

    override fun close() {
        super.close()
    }
}