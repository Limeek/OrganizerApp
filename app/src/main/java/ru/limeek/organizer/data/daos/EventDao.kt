package ru.limeek.organizer.data.daos

import androidx.room.*
import org.joda.time.DateTime
import ru.limeek.organizer.data.database.DateConverter
import ru.limeek.organizer.domain.entities.EventWithLocation
import ru.limeek.organizer.domain.entities.event.Event

@Dao
interface EventDao{
    @Query("SELECT * FROM EVENTS E LEFT JOIN LOCATIONS L ON E.event_location_id = L.location_id")
    suspend fun getEventsWithLocationAll() : List<EventWithLocation>

    @Query("SELECT * FROM EVENTS E LEFT JOIN LOCATIONS L ON E.event_location_id = L.location_id WHERE id = :id")
    suspend fun getEventWithLocationById(id : Long) : EventWithLocation

    @Query("SELECT * FROM EVENTS E LEFT JOIN LOCATIONS L ON E.event_location_id = L.location_id WHERE date_time LIKE :date || '%'")
    suspend fun getEventsWithLocationByDate(@TypeConverters(DateConverter::class) date : DateTime) : List<EventWithLocation>

    @Query("SELECT * FROM EVENTS WHERE event_location_id = :locationId")
    suspend fun getEventsByLocationId(locationId: Long) : List<Event>

    @Insert
    suspend fun insert(event: Event): Long

    @Delete
    suspend fun delete(event: Event?)

    @Update
    suspend fun update(event: Event?)

    @Update
    suspend fun update(events : List<Event>)
}