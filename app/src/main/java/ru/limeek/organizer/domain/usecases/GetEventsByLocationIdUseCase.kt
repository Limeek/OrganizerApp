package ru.limeek.organizer.domain.usecases

import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.domain.entities.event.Event
import javax.inject.Inject

class GetEventsByLocationIdUseCase @Inject constructor(private val eventRepository: EventRepository){
    suspend fun execute(locId: Long): List<Event> {
        return eventRepository.getEventsByLocationId(locId)
    }
}