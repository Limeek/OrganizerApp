package ru.limeek.organizer.presentation.di.modules.fragments

import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.presentation.di.scopes.FragmentScope
import ru.limeek.organizer.presentation.viewmodels.CalendarViewModel
import ru.limeek.organizer.presentation.viewmodels.factories.CalendarViewModelFactory
import ru.limeek.organizer.presentation.views.CalendarFragment

@Module
class CalendarFragmentModule {
    @FragmentScope
    @Provides
    fun provideCalendarViewModel(fragment: CalendarFragment,
                                 sharedPrefsRepo: SharedPrefsRepository): CalendarViewModel {
        return ViewModelProviders
                .of(fragment, CalendarViewModelFactory(sharedPrefsRepo))
                .get(CalendarViewModel::class.java)
    }
}