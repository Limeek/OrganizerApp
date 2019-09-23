package ru.limeek.organizer.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.model.location.Location
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.data.repository.LocationRepository
import javax.inject.Inject

class DeleteEventUseCase @Inject constructor(private val eventRepository: EventRepository,
                                             private val locationRepository: LocationRepository) {
    suspend fun execute(event: Event){
        withContext(Dispatchers.IO) {
            if (!event.location!!.createdByUser)
                locationRepository.delete(event.location!!)
            eventRepository.delete(event)
        }
    }
}