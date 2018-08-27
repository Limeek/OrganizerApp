package ru.limeek.organizer.model.Event

import android.arch.persistence.room.*
import io.reactivex.Flowable
import org.joda.time.DateTime
import ru.limeek.organizer.app.App
import ru.limeek.organizer.database.DateConverter
import ru.limeek.organizer.model.EventWithLocation

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

    fun getEventById(id : Long) : Flowable<Event>{
        return getEventWithLocationById(id)
                .map{
                    val event = it.event
                    event.location = it.location
                    return@map event
                }
    }

    fun getEventsByDate(date : DateTime) : Flowable<List<Event>>{
        return getEventsWithLocationByDate(date)
                .map{
                    val eventsList : MutableList<Event> = mutableListOf()
                    for(e : EventWithLocation in it){
                        val event = e.event
                        event.location = e.location
                        eventsList.add(event)
                    }
                    return@map eventsList
                }
    }

    fun getAllEvents() : Flowable<List<Event>>{
        return getEventsWithLocationAll()
                .map{
                    val eventsList : MutableList<Event> = mutableListOf()
                    for(e : EventWithLocation in it){
                        val event = e.event
                        event.location = e.location
                        eventsList.add(event)
                    }
                    return@map eventsList
                }
    }

    fun insertEvent(event: Event) : Long{
        val locationId : Long
        val eventId : Long
        when {
            event.location != null -> {
                locationId = App.instance.database.locationDao().insert(event.location)
                event.locationId = locationId
                eventId = insert(event)
            }

            event.locationId != null -> eventId = insert(event)

            else -> {
                event.locationId = null
                eventId = insert(event)
            }
        }
        return eventId
    }

    fun updateEventWithAddedLocation(event: Event){
        if(event.locationId != null) update(event)
        else{
            val locationId = App.instance.database.locationDao().insert(event.location)
            event.locationId = locationId
            update(event)
        }
    }

    fun updateEventWithCustomToCustomLoc(event: Event){
        val locationId = App.instance.database.locationDao().upsert(event.location!!)
        event.locationId = locationId
        update(event)
    }

    fun updateEventWithUserToCustomLoc(event: Event){
        val locationId = App.instance.database.locationDao().insert(event.location!!)
        event.locationId = locationId
        update(event)
    }

    fun updateEventWithCustomToUserLoc(event: Event){
        update(event)
        App.instance.database.locationDao().delete(event.location)
    }

    fun updateEventWithUserLocDelete(event: Event){
        event.locationId = null
        update(event)
    }

    fun updateEventWithCustLocDelete(event: Event){
        event.locationId = null
        update(event)
        App.instance.database.locationDao().delete(event.location)
    }

    fun deleteEvent(event: Event){
        if(event.location != null && !event.location!!.createdByUser){
            delete(event)
            App.instance.database.locationDao().delete(event.location)
        }
        else
            delete(event)
    }
}