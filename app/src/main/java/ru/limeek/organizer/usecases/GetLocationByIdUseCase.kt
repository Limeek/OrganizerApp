package ru.limeek.organizer.usecases

import ru.limeek.organizer.data.model.location.Location
import ru.limeek.organizer.data.repository.LocationRepository
import javax.inject.Inject

class GetLocationByIdUseCase @Inject constructor(private val locationRepository: LocationRepository){
    suspend fun execute(id: Long): Location? {
        return locationRepository.getLocationById(id)
    }
}