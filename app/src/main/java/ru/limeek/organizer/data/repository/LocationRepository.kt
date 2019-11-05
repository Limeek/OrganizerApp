package ru.limeek.organizer.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.limeek.organizer.data.daos.EventDao
import ru.limeek.organizer.domain.entities.location.Location
import ru.limeek.organizer.data.daos.LocationDao
import javax.inject.Inject

class LocationRepository @Inject constructor(private var locationDao: LocationDao,
                                             eventDao: EventDao) {
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
}