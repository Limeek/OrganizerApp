package ru.limeek.organizer.usecases

import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.repository.EventRepository
import javax.inject.Inject

class GetAllEventsUseCase @Inject constructor(private val eventsRepo: EventRepository){
    suspend fun execute(): List<Event> {
        return eventsRepo.getAllEvents()
    }
}