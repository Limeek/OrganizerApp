package ru.limeek.organizer.di.components

import dagger.Subcomponent
import ru.limeek.organizer.di.modules.ViewModelModule
import ru.limeek.organizer.di.scopes.PresenterScope
import ru.limeek.organizer.views.*

@Subcomponent(modules = [ViewModelModule::class])
@PresenterScope
interface ViewViewModelComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(calendarFragment: CalendarFragment)
    fun inject(eventsFragment: EventsFragment)
}