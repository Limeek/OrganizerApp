package ru.limeek.organizer.model

import android.arch.persistence.room.Embedded
import ru.limeek.organizer.model.event.Event
import ru.limeek.organizer.model.location.Location

class EventWithLocation {
    @Embedded lateinit var event : Event
    @Embedded var location: Location? = null
}