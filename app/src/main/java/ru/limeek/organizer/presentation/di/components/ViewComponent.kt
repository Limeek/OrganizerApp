package ru.limeek.organizer.presentation.di.components

import dagger.Subcomponent
import ru.limeek.organizer.presentation.di.modules.ViewModelModule
import ru.limeek.organizer.presentation.di.scopes.ViewScope
import ru.limeek.organizer.presentation.views.*

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