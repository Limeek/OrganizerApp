package ru.limeek.organizer.domain.usecases

import ru.limeek.organizer.domain.entities.event.Event
import ru.limeek.organizer.data.repository.EventRepository
import javax.inject.Inject

class InsertEventUseCase @Inject constructor(private val eventRepository: EventRepository){
    suspend fun execute(event: Event): Long{
        return eventRepository.insert(event)
    }
}