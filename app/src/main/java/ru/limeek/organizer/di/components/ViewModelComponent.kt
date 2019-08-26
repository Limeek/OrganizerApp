package ru.limeek.organizer.di.components

import dagger.Subcomponent
import ru.limeek.organizer.di.modules.RepositoryModule
import ru.limeek.organizer.di.scopes.ViewModelScope
import ru.limeek.organizer.viewmodels.CalendarViewModel
import ru.limeek.organizer.viewmodels.MainViewModel

@ViewModelScope
@Subcomponent(modules = [RepositoryModule::class])
interface ViewModelComponent {
    fun inject(calendarViewModel: CalendarViewModel)
    fun inject(mainViewModel: MainViewModel)
}