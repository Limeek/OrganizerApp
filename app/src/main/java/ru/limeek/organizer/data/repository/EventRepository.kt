package ru.limeek.organizer.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import ru.limeek.organizer.data.model.EventWithLocation
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.model.event.EventDao
import ru.limeek.organizer.data.model.location.LocationDao
import javax.inject.Inject

class EventRepository @Inject constructor(private var eventDao: EventDao,
                                          private var locationDao: LocationDao) {

    private suspend fun getEventsWithLocationAll(): List<EventWithLocation> {
        return eventDao.getEventsWithLocationAll()
    }

    private suspend fun getEventsWithLocationByDate(date: DateTime): List<EventWithLocation> {
        return eventDao.getEventsWithLocationByDate(date)
    }

    private suspend fun getEventWithLocationById(id: Long): EventWithLocation? {
        return eventDao.getEventWithLocationById(id)
    }

    private suspend fun getEventsByLocationId(locationId: Long): List<Event> {
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

    suspend fun update(event: Event) {
        withContext(Dispatchers.IO) {
            eventDao.update(event)
        }
    }

    suspend fun getEventById(id: Long): Event? {
        return withContext(Dispatchers.IO) {
            val eventWithLocation = getEventWithLocationById(id)
            val event = eventWithLocation?.event
            event?.location = eventWithLocation?.location
            event
        }
    }

    suspend fun getEventsByDate(date: DateTime): List<Event> {
        return withContext(Dispatchers.IO) {
            val eventsWithLocation = getEventsWithLocationByDate(date)
            val eventsList: MutableList<Event> = mutableListOf()

            eventsWithLocation.forEach {
                val event = it.event
                event?.location = it.location
                eventsList.add(event!!)
            }

           eventsList
        }
    }

    suspend fun getAllEvents(): List<Event> {
        return withContext(Dispatchers.IO) {
            val eventsWithLocation = getEventsWithLocationAll()
            val eventsList: MutableList<Event> = mutableListOf()
            eventsWithLocation.forEach{
                val event = it.event
                event?.location = it.location
                eventsList.add(event!!)
            }
            eventsList
        }
    }

    suspend fun insertEvent(event: Event): Long {
        return withContext(Dispatchers.IO) {
            val locationId: Long
            val eventId: Long
            when {
                event.location != null -> {
                    locationId = locationDao.insert(event.location)
                    event.locationId = locationId
                    eventId = insert(event)
                }

                event.locationId != null -> eventId = insert(event)

                else -> {
                    event.locationId = null
                    eventId = insert(event)
                }
            }
            eventId
        }
    }

    suspend fun updateEventWithAddedLocation(event: Event) {
        withContext(Dispatchers.IO){
            if (event.locationId != null) update(event)
            else {
                val locationId = locationDao.insert(event.location)
                event.locationId = locationId
                update(event)
            }
        }
    }

    suspend fun updateEventWithCustomToCustomLoc(event: Event) {
        withContext(Dispatchers.IO) {
            val locationId = locationDao.upsert(event.location!!)
            event.locationId = locationId
            update(event)
        }
    }

    suspend fun updateEventWithUserToCustomLoc(event: Event) {
        withContext(Dispatchers.IO) {
            val locationId = locationDao.insert(event.location!!)
            event.locationId = locationId
            update(event)
        }
    }

    suspend fun updateEventWithCustomToUserLoc(event: Event) {
        withContext(Dispatchers.IO) {
            update(event)
            locationDao.delete(event.location)
        }
    }

    suspend fun updateEventWithUserLocDelete(event: Event) {
        withContext(Dispatchers.IO) {
            event.locationId = null
            update(event)
        }
    }

    suspend fun updateEventWithCustLocDelete(event: Event) {
        withContext(Dispatchers.IO) {
            event.locationId = null
            update(event)
            locationDao.delete(event.location)
        }
    }

    suspend fun deleteEvent(event: Event) {
        withContext(Dispatchers.IO) {
            if (event.location != null && !event.location!!.createdByUser) {
                delete(event)
                locationDao.delete(event.location)
            } else
                delete(event)
        }
    }
}