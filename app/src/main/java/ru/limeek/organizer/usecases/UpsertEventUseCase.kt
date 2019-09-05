package ru.limeek.organizer.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.model.location.Location
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.data.repository.LocationRepository
import javax.inject.Inject

class UpsertEventUseCase @Inject constructor(private val eventRepository: EventRepository,
                                             private val locationRepository: LocationRepository) : UseCase<Event, Long> {

    override suspend fun execute(event: Event): Long {
        return withContext(Dispatchers.IO) {
            if (event.id == 0L)
                insertEvent(event)
            else
                updateEvent(event)
        }
    }

    private suspend fun insertEvent(event: Event): Long {
        if (event.location != null)
            event.locationId = insertLocationIfNeeded(event.location!!)
        return eventRepository.insert(event)
    }

    private suspend fun updateEvent(event: Event): Long {
        if(event.location != null)
            event.locationId = insertLocationIfNeeded(event.location!!)
        return eventRepository.update(event)
    }

    private suspend fun insertLocationIfNeeded(location: Location): Long? {
        return if (location.id == 0L)
            locationRepository.insert(location)
        else
            null
    }

}