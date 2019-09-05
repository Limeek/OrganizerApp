package ru.limeek.organizer.usecases

import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.model.location.Location
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.data.repository.LocationRepository
import javax.inject.Inject

class DeleteLocationUseCase @Inject constructor(private val locationRepo: LocationRepository,
                                                private val eventRepo: EventRepository) : UseCase<Location, Unit> {

    override suspend fun execute(location: Location) {
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