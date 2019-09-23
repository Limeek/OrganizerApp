package ru.limeek.organizer.usecases;

import org.joda.time.DateTime
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.repository.EventRepository
import javax.inject.Inject

class GetEventsByDateUseCase @Inject constructor(private val eventRepo: EventRepository){
    suspend fun execute(dateTime: DateTime): List<Event> {
        return eventRepo.getEventsByDate(dateTime)
    }
}
