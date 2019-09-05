package ru.limeek.organizer.usecases


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.limeek.organizer.data.model.location.Location
import ru.limeek.organizer.data.repository.LocationRepository
import javax.inject.Inject

class UpsertLocationUseCase @Inject constructor(private val locationRepo: LocationRepository): UseCase<Location, Long> {

    override suspend fun execute(location: Location): Long {
        return withContext(Dispatchers.IO){
            if(location.id != 0L)
                updateLocation(location)
            else
                insertLocation(location)
        }
    }

    private suspend fun insertLocation(location: Location): Long{
        return locationRepo.insert(location)
    }

    private suspend fun updateLocation(location: Location): Long{
        return locationRepo.update(location)
    }
}