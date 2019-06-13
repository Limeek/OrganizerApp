package ru.limeek.organizer.di.components

import dagger.Subcomponent
import ru.limeek.organizer.di.modules.RepositoryModule
import ru.limeek.organizer.di.scopes.PresenterScope
import ru.limeek.organizer.presenters.NewsAdapterPresenter
import ru.limeek.organizer.presenters.WeatherPresenterImpl
import ru.limeek.organizer.presenters.EventDetailsPresenterImpl
import ru.limeek.organizer.presenters.LocationDetailsPresenterImpl
import ru.limeek.organizer.presenters.LocationPresenterImpl
import ru.limeek.organizer.presenters.LocationsAdapterPresenter

@PresenterScope
@Subcomponent(modules = [RepositoryModule::class])
interface PresenterComponent {
    fun inject(eventDetailsPresenterImpl: EventDetailsPresenterImpl)
    fun inject(locationPresenterImpl: LocationPresenterImpl)
    fun inject(locationDetailsPresenterImpl: LocationDetailsPresenterImpl)
    fun inject(weatherPresenterImpl: WeatherPresenterImpl)
    fun inject(locationsAdapterPresenter: LocationsAdapterPresenter)
    fun inject(newsAdapterPresenter: NewsAdapterPresenter)

}