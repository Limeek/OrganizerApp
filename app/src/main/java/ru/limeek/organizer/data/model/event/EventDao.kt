package ru.limeek.organizer.data.model.event

import androidx.room.*
import io.reactivex.Flowable
import org.joda.time.DateTime
import ru.limeek.organizer.database.DateConverter
import ru.limeek.organizer.data.model.EventWithLocation

@Dao
abstract class EventDao{
    @Query("SELECT * FROM EVENTS E LEFT JOIN LOCATIONS L ON E.event_location_id = L.location_id")
    abstract fun getEventsWithLocationAll() : Flowable<List<EventWithLocation>>

    @Query("SELECT * FROM EVENTS E LEFT JOIN LOCATIONS L ON E.event_location_id = L.location_id WHERE id = :id")
    abstract fun getEventWithLocationById(id : Long) : Flowable<EventWithLocation>

    @Query("SELECT * FROM EVENTS E LEFT JOIN LOCATIONS L ON E.event_location_id = L.location_id WHERE date_time LIKE :date || '%'")
    abstract fun getEventsWithLocationByDate(@TypeConverters(DateConverter::class) date : DateTime) : Flowable<List<EventWithLocation>>

    @Query("SELECT * FROM EVENTS WHERE event_location_id = :locationId")
    abstract fun getEventsByLocationId(locationId: Long) : Flowable<List<Event>>

    @Insert
    abstract fun insert(event: Event) : Long

    @Delete
    abstract fun delete(event: Event?)

    @Update
    abstract fun update(event: Event?)

    @Update
    abstract fun update(events : List<Event>)
}