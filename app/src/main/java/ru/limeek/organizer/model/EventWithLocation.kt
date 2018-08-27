package ru.limeek.organizer.model

import android.arch.persistence.room.Embedded
import ru.limeek.organizer.model.Event.Event
import ru.limeek.organizer.model.Location.Location

class EventWithLocation {
    @Embedded lateinit var event : Event
    @Embedded var location: Location? = null
}