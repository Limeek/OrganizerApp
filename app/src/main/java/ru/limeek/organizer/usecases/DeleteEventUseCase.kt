package ru.limeek.organizer.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.model.location.Location
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.data.repository.LocationRepository
import javax.inject.Inject

class DeleteEventUseCase @Inject constructor(private val eventRepository: EventRepository,
                                             private val locationRepository: LocationRepository): UseCase<Event, Unit> {
    override suspend fun execute(event: Event) {
        withContext(Dispatchers.IO) {
            if (event.location?.createdByUser == false)
                deleteLocation(event.location!!)
            deleteEvent(event)
        }
    }

    private suspend fun deleteEvent(event: Event){
        eventRepository.delete(event)
    }

    private suspend fun deleteLocation(location: Location){
        locationRepository.delete(location)
    }
}