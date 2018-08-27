package ru.limeek.organizer.di.components

import dagger.Subcomponent
import ru.limeek.organizer.calendar.presenter.CalendarPresenterImpl
import ru.limeek.organizer.di.modules.RepositoryModule
import ru.limeek.organizer.di.scopes.PresenterScope
import ru.limeek.organizer.digest.news.presenter.NewsAdapterPresenter
import ru.limeek.organizer.digest.weather.presenter.WeatherPresenterImpl
import ru.limeek.organizer.event.eventdetails.presenter.EventDetailsPresenterImpl
import ru.limeek.organizer.event.presenter.EventsAdapterPresenter
import ru.limeek.organizer.event.presenter.EventsPresenterImpl
import ru.limeek.organizer.locations.locationdetails.presenter.LocationDetailsPresenterImpl
import ru.limeek.organizer.locations.presenter.LocationPresenterImpl
import ru.limeek.organizer.locations.presenter.LocationsAdapterPresenter

@PresenterScope
@Subcomponent(modules = [RepositoryModule::class])
interface PresenterComponent {
    fun inject(calendarPresenterImpl: CalendarPresenterImpl)
    fun inject(eventsPresenterImpl: EventsPresenterImpl)
    fun inject(eventDetailsPresenterImpl: EventDetailsPresenterImpl)
    fun inject(locationPresenterImpl: LocationPresenterImpl)
    fun inject(locationDetailsPresenterImpl: LocationDetailsPresenterImpl)
    fun inject(weatherPresenterImpl: WeatherPresenterImpl)
    fun inject(eventsAdapterPresenter: EventsAdapterPresenter)
    fun inject(locationsAdapterPresenter: LocationsAdapterPresenter)
    fun inject(newsAdapterPresenter: NewsAdapterPresenter)
}