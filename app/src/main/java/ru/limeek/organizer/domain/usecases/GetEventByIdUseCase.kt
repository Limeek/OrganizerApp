package ru.limeek.organizer.domain.usecases

import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.domain.entities.event.Event
import javax.inject.Inject

class GetEventByIdUseCase @Inject constructor(private val eventRepo: EventRepository){
    suspend fun execute(id: Long): Event? {
        return eventRepo.getEventById(id)
    }
}