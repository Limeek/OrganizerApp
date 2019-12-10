package ru.limeek.organizer.domain.usecases

import ru.limeek.organizer.data.repository.LocationRepository
import ru.limeek.organizer.domain.entities.location.Location
import javax.inject.Inject

class GetLocationByIdUseCase @Inject constructor(private val locationRepository: LocationRepository){
    suspend fun execute(id: Long): Location? {
        return locationRepository.getLocationById(id)
    }
}