package ru.limeek.organizer.domain.usecases

import org.joda.time.DateTime
import ru.limeek.organizer.domain.entities.event.Event
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.presentation.util.Constants
import javax.inject.Inject

class GetEventsByCachedDateUseCase @Inject constructor(private val sharedPrefsRepository: SharedPrefsRepository,
                                                       private val eventsRepository: EventRepository){
    suspend fun execute(): List<Event> {
        return eventsRepository.getEventsByDate(getCurrentCachedDate())
    }

    private fun getCurrentCachedDate(): DateTime{
        return sharedPrefsRepository.getDateTime(Constants.CACHED_DATE)
    }
}