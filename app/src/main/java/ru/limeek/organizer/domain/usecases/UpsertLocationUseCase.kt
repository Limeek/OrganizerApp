package ru.limeek.organizer.domain.usecases


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.limeek.organizer.data.repository.LocationRepository
import ru.limeek.organizer.domain.entities.location.Location
import javax.inject.Inject

class UpsertLocationUseCase @Inject constructor(private val locationRepo: LocationRepository){

    suspend fun execute(location: Location?){
        return withContext(Dispatchers.IO){
            if(location?.id != 0L)
                updateLocation(location!!)
            else
                insertLocation(location)
        }
    }

    private suspend fun insertLocation(location: Location){
        locationRepo.insert(location)
    }

    private suspend fun updateLocation(location: Location){
        locationRepo.update(location)
    }
}