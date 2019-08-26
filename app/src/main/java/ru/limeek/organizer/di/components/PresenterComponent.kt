package ru.limeek.organizer.di.components

import dagger.Subcomponent
import ru.limeek.organizer.di.modules.RepositoryModule
import ru.limeek.organizer.di.scopes.ViewModelScope
import ru.limeek.organizer.presenters.EventDetailsPresenterImpl
import ru.limeek.organizer.presenters.LocationDetailsPresenterImpl

@ViewModelScope
@Subcomponent(modules = [RepositoryModule::class])
interface PresenterComponent {
    fun inject(eventDetailsPresenterImpl: EventDetailsPresenterImpl)
    fun inject(locationDetailsPresenterImpl: LocationDetailsPresenterImpl)
}