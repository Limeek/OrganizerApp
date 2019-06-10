package ru.limeek.organizer.data.repository

import io.reactivex.Flowable
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.model.event.EventDao
import ru.limeek.organizer.data.model.location.Location
import ru.limeek.organizer.data.model.location.LocationDao
import javax.inject.Inject

class LocationRepository @Inject constructor(var locationDao: LocationDao,
                                             var eventDao: EventDao) {
    fun insert(location: Location): Long{
        return locationDao.insert(location)
    }

    fun update(location: Location){
        locationDao.update(location)
    }

    fun delete(location: Location){
        locationDao.delete(location)
    }

    fun getUserCreatedLocations(): Flowable<List<Location>>{
        return locationDao.getUserCreatedLocations()
    }

    fun getLocationById(id: Long): Flowable<Location>{
        return locationDao.getLocationById(id)
    }

    fun deleteLocation(location: Location) : Flowable<Unit> {
        return eventDao.getEventsByLocationId(location.id)
                .flatMap{ events ->
                    Flowable.fromCallable {
                        for(event : Event in events)
                            event.locationId = null
                        eventDao.update(events)
                        delete(location)
                    }
                }
    }
}