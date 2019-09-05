package ru.limeek.organizer.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import ru.limeek.organizer.data.model.EventWithLocation
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.model.event.EventDao
import ru.limeek.organizer.data.model.location.LocationDao
import javax.inject.Inject

class EventRepository @Inject constructor(private var eventDao: EventDao) {

    suspend fun getEventsByLocationId(locationId: Long): List<Event> {
        return eventDao.getEventsByLocationId(locationId)
    }

    suspend fun insert(event: Event): Long {
        return withContext(Dispatchers.IO) {
            eventDao.insert(event)
        }
    }

    suspend fun delete(event: Event) {
        withContext(Dispatchers.IO) {
            eventDao.delete(event)
        }
    }

    suspend fun update(event: Event): Long {
        return withContext(Dispatchers.IO) {
            eventDao.update(event)
        }
    }

    suspend fun update(events: List<Event>){
        return withContext(Dispatchers.IO){
            eventDao.update(events)
        }
    }

    suspend fun getEventById(id: Long): Event? {
        return withContext(Dispatchers.IO) {
            val eventWithLocation = getEventWithLocationById(id)
            initLocationInEvent(eventWithLocation)
        }
    }

    suspend fun getEventsByDate(date: DateTime): List<Event> {
        return withContext(Dispatchers.IO) {
            val eventsWithLocation = getEventsWithLocationByDate(date)
            initLocationInEvents(eventsWithLocation)
        }
    }

    suspend fun getAllEvents(): List<Event> {
        return withContext(Dispatchers.IO) {
            val eventsWithLocation = getEventsWithLocationAll()
            initLocationInEvents(eventsWithLocation)
        }
    }

    private fun initLocationInEvent(eventWithLocation: EventWithLocation?): Event?{
        val event = eventWithLocation?.event
        event?.location = eventWithLocation?.location
        return event
    }

    private fun initLocationInEvents(eventsWithLocation: List<EventWithLocation>): List<Event>{
        val events = mutableListOf<Event>()
        eventsWithLocation.forEach{
            val event = it.event
            event?.location = it.location
            events.add(event!!)
        }
        return events
    }

    private suspend fun getEventsWithLocationAll(): List<EventWithLocation> {
        return eventDao.getEventsWithLocationAll()
    }

    private suspend fun getEventsWithLocationByDate(date: DateTime): List<EventWithLocation> {
        return eventDao.getEventsWithLocationByDate(date)
    }

    private suspend fun getEventWithLocationById(id: Long): EventWithLocation? {
        return eventDao.getEventWithLocationById(id)
    }
}