package ru.limeek.organizer.data.model

import androidx.room.Embedded
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.model.location.Location

class EventWithLocation {
    @Embedded lateinit var event : Event
    @Embedded var location: Location? = null
}