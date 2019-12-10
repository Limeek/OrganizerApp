package ru.limeek.organizer.domain.usecases

import ru.limeek.organizer.data.repository.LocationRepository
import ru.limeek.organizer.domain.entities.location.Location
import javax.inject.Inject

class GetUserCreatedLocationsUseCase @Inject constructor(private val locationRepository: LocationRepository){

    suspend fun execute(): List<Location> {
        return locationRepository.getUserCreatedLocations()
    }

}