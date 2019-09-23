package ru.limeek.organizer.usecases

import ru.limeek.organizer.data.model.location.Location
import ru.limeek.organizer.data.repository.LocationRepository
import javax.inject.Inject

class InsertLocationUseCase @Inject constructor(private val locationRepository: LocationRepository){
    suspend fun execute(location: Location): Long{
        return locationRepository.insert(location)
    }
}