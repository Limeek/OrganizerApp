package ru.limeek.organizer.domain.usecases

import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.data.repository.LocationRepository
import ru.limeek.organizer.domain.entities.event.Event
import ru.limeek.organizer.domain.entities.location.Location
import javax.inject.Inject

class DeleteLocationUseCase @Inject constructor(private val locationRepo: LocationRepository,
                                                private val eventRepo: EventRepository) {

    suspend fun execute(location: Location) {
        if (location.createdByUser)
            deleteAllLocationIdsInEvents(location)
        deleteLocation(location)
    }

    private suspend fun deleteLocation(location: Location) {
        locationRepo.delete(location)
    }

    private suspend fun deleteAllLocationIdsInEvents(location: Location) {
        val events = getAllEventsWithLocationId(location.id)
        if (events.isNotEmpty()) {
            events.forEach { it.locationId = null }
            updateAllEvents(events)
        }
    }

    private suspend fun getAllEventsWithLocationId(locationId: Long): List<Event> {
        return eventRepo.getEventsByLocationId(locationId)
    }

    private suspend fun updateAllEvents(events: List<Event>) {
        eventRepo.update(events)
    }
}