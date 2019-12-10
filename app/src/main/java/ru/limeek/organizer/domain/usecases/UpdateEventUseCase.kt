package ru.limeek.organizer.domain.usecases

import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.domain.entities.event.Event
import javax.inject.Inject

class UpdateEventUseCase @Inject constructor(private val eventsRepo: EventRepository) {
    suspend fun execute(event: Event){
        eventsRepo.update(event)
    }
}