package ru.limeek.organizer.usecases

import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.repository.EventRepository
import javax.inject.Inject

class GetEventsByLocationIdUseCase @Inject constructor(private val eventRepository: EventRepository){
    suspend fun execute(locId: Long): List<Event> {
        return eventRepository.getEventsByLocationId(locId)
    }
}