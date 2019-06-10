package ru.limeek.organizer.data.repository

import io.reactivex.Flowable
import org.joda.time.DateTime
import ru.limeek.organizer.data.model.EventWithLocation
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.model.event.EventDao
import ru.limeek.organizer.data.model.location.LocationDao
import javax.inject.Inject

class EventRepository @Inject constructor(var eventDao: EventDao,
                                          var locationDao: LocationDao) {

    private fun getEventsWithLocationAll(): Flowable<List<EventWithLocation>> {
        return eventDao.getEventsWithLocationAll()
    }

    private fun getEventsWithLocationByDate(date: DateTime) : Flowable<List<EventWithLocation>> {
        return eventDao.getEventsWithLocationByDate(date)
    }

    private fun getEventWithLocationById(id: Long): Flowable<EventWithLocation>{
        return eventDao.getEventWithLocationById(id)
    }

    private fun getEventsByLocationId(locationId: Long): Flowable<List<Event>>{
        return eventDao.getEventsByLocationId(locationId)
    }

    fun insert(event: Event): Long{
        return eventDao.insert(event)
    }

    fun delete(event: Event){
        eventDao.delete(event)
    }

    fun update(event: Event){
        eventDao.update(event)
    }

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
        return eventId
    }

    fun updateEventWithAddedLocation(event: Event){
        if(event.locationId != null) update(event)
        else{
            val locationId = locationDao.insert(event.location)
            event.locationId = locationId
            update(event)
        }
    }

    fun updateEventWithCustomToCustomLoc(event: Event){
        val locationId = locationDao.upsert(event.location!!)
        event.locationId = locationId
        update(event)
    }

    fun updateEventWithUserToCustomLoc(event: Event){
        val locationId = locationDao.insert(event.location!!)
        event.locationId = locationId
        update(event)
    }

    fun updateEventWithCustomToUserLoc(event: Event){
        update(event)
        locationDao.delete(event.location)
    }

    fun updateEventWithUserLocDelete(event: Event){
        event.locationId = null
        update(event)
    }

    fun updateEventWithCustLocDelete(event: Event){
        event.locationId = null
        update(event)
        locationDao.delete(event.location)
    }

    fun deleteEvent(event: Event){
        if(event.location != null && !event.location!!.createdByUser){
            delete(event)
            locationDao.delete(event.location)
        }
        else
            delete(event)
    }
}