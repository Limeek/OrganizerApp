package ru.limeek.organizer.data.repository

import io.reactivex.Flowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.model.event.EventDao
import ru.limeek.organizer.data.model.location.Location
import ru.limeek.organizer.data.model.location.LocationDao
import javax.inject.Inject

class LocationRepository @Inject constructor(var locationDao: LocationDao,
                                             var eventDao: EventDao) {
    suspend fun insert(location: Location): Long{
        return withContext(Dispatchers.IO) {
            locationDao.insert(location)
        }
    }

    suspend fun update(location: Location){
        return withContext(Dispatchers.IO) {
            locationDao.update(location)
        }
    }

    suspend fun delete(location: Location){
        withContext(Dispatchers.IO){
            locationDao.delete(location)
        }
    }

    suspend fun getUserCreatedLocations(): List<Location>{
        return withContext(Dispatchers.IO){
            locationDao.getUserCreatedLocations()
        }
    }

    suspend fun getLocationById(id: Long): Location?{
        return withContext(Dispatchers.IO){
            locationDao.getLocationById(id)
        }
    }

    suspend fun deleteLocation(location: Location){
        return withContext(Dispatchers.IO){
            val events = eventDao.getEventsByLocationId(location.id)
            events.forEach {
                it.locationId = null
            }
            eventDao.update(events)
            delete(location)
        }
    }
}