package ru.limeek.organizer.data.model.event

import androidx.room.*
import org.joda.time.DateTime
import ru.limeek.organizer.data.model.EventWithLocation
import ru.limeek.organizer.database.DateConverter

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
    suspend fun insert(event: Event) : Long

    @Delete
    suspend fun delete(event: Event?)

    @Update
    suspend fun update(event: Event?): Long

    @Update
    suspend fun update(events : List<Event>)
}