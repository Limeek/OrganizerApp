package ru.limeek.organizer.di.components

import dagger.Subcomponent
import ru.limeek.organizer.calendar.view.CalendarActivity
import ru.limeek.organizer.di.modules.PresenterModule
import ru.limeek.organizer.di.scopes.ViewScope
import ru.limeek.organizer.event.eventdetails.view.EventDetailsActivity
import ru.limeek.organizer.event.view.EventsFragment
import ru.limeek.organizer.locations.locationdetails.view.LocationDetailsActivity
import ru.limeek.organizer.locations.view.LocationActivity

@Subcomponent(modules = [PresenterModule::class])
@ViewScope
interface ViewComponent {
    fun inject(calendarActivity: CalendarActivity)
    fun inject(eventsFragment: EventsFragment)
    fun inject(eventDetailsActivity: EventDetailsActivity)
    fun inject(locationActivity: LocationActivity)
    fun inject(locationDetailsActivity: LocationDetailsActivity)
}