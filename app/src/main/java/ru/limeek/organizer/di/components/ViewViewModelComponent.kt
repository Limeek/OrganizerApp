package ru.limeek.organizer.di.components

import dagger.Subcomponent
import ru.limeek.organizer.di.modules.ViewModelModule
import ru.limeek.organizer.di.scopes.ViewModelScope
import ru.limeek.organizer.views.*

@Subcomponent(modules = [ViewModelModule::class])
@ViewModelScope
interface ViewViewModelComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(calendarFragment: CalendarFragment)
    fun inject(eventsFragment: EventsFragment)
    fun inject(weatherFragment: WeatherFragment)
    fun inject(newsFragment: NewsFragment)
    fun inject(locationActivity: LocationActivity)
    fun inject(eventDetailsActivity: EventDetailsActivity)
}