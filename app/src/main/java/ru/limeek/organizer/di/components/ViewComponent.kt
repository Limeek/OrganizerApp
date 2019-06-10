package ru.limeek.organizer.di.components

import dagger.Subcomponent
import ru.limeek.organizer.di.modules.PresenterModule
import ru.limeek.organizer.di.scopes.ViewScope
import ru.limeek.organizer.views.*

@Subcomponent(modules = [PresenterModule::class])
@ViewScope
interface ViewComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(eventsFragment: EventsFragment)
    fun inject(eventDetailsActivity: EventDetailsActivity)
    fun inject(locationActivity: LocationActivity)
    fun inject(locationDetailsActivity: LocationDetailsActivity)
    fun inject(calendarFragment: CalendarFragment)
}