package ru.limeek.organizer.presentation.di.modules.fragments

import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.domain.usecases.GetEventsByCachedDateUseCase
import ru.limeek.organizer.presentation.di.scopes.FragmentScope
import ru.limeek.organizer.presentation.viewmodels.EventsViewModel
import ru.limeek.organizer.presentation.viewmodels.factories.EventsViewModelFactory
import ru.limeek.organizer.presentation.views.EventsFragment

@Module
class EventsFragmentModule {
    @Provides
    @FragmentScope
    fun provideEventsViewModel(fragment: EventsFragment,
                                    sharedPrefsRepo: SharedPrefsRepository,
                                    getEventsByCachedDateUseCase: GetEventsByCachedDateUseCase): EventsViewModel {
        return ViewModelProviders
                .of(fragment, EventsViewModelFactory(sharedPrefsRepo, getEventsByCachedDateUseCase))
                .get(EventsViewModel::class.java)
    }
}