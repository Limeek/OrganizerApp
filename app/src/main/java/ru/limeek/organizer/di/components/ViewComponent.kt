package ru.limeek.organizer.di.components

import dagger.Subcomponent
import ru.limeek.organizer.di.modules.ViewModelModule
import ru.limeek.organizer.di.scopes.ViewScope
import ru.limeek.organizer.views.*

@Subcomponent(modules = [ViewModelModule::class])
@ViewScope
interface ViewComponent {
    fun inject(locationActivity: LocationActivity)
    fun inject(locationDetailsActivity: LocationDetailsActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(calendarFragment: CalendarFragment)
    fun inject(eventsFragment: EventsFragment)
    fun inject(eventDetailsActivity: EventDetailsActivity)
}