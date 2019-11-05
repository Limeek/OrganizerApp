package ru.limeek.organizer.domain.entities

import androidx.room.Embedded
import ru.limeek.organizer.domain.entities.event.Event
import ru.limeek.organizer.domain.entities.location.Location

class EventWithLocation {
    @Embedded var event : Event? = null
    @Embedded var location: Location? = null
}