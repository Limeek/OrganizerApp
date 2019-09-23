package ru.limeek.organizer.usecases

import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.repository.EventRepository
import javax.inject.Inject

class UpdateEventUseCase @Inject constructor(private val eventsRepo: EventRepository) {
    suspend fun execute(event: Event): Long{
        return eventsRepo.update(event)
    }
}