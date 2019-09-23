package ru.limeek.organizer.usecases

import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.repository.EventRepository
import javax.inject.Inject

class GetEventByIdUseCase @Inject constructor(private val eventRepo: EventRepository){
    suspend fun execute(id: Long): Event? {
        return eventRepo.getEventById(id)
    }
}