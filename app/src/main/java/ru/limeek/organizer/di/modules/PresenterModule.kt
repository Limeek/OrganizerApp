package ru.limeek.organizer.di.modules

import dagger.Module
import dagger.Provides
import ru.limeek.organizer.calendar.presenter.CalendarPresenter
import ru.limeek.organizer.calendar.presenter.CalendarPresenterImpl
import ru.limeek.organizer.calendar.view.AppCalendarView
import ru.limeek.organizer.di.scopes.ViewScope
import ru.limeek.organizer.event.eventdetails.presenter.EventDetailsPresenter
import ru.limeek.organizer.event.eventdetails.presenter.EventDetailsPresenterImpl
import ru.limeek.organizer.event.eventdetails.view.EventDetailsView
import ru.limeek.organizer.event.presenter.EventsPresenter
import ru.limeek.organizer.event.presenter.EventsPresenterImpl
import ru.limeek.organizer.event.view.EventFragmentView
import ru.limeek.organizer.locations.locationdetails.presenter.LocationDetailsPresenter
import ru.limeek.organizer.locations.locationdetails.presenter.LocationDetailsPresenterImpl
import ru.limeek.organizer.locations.locationdetails.view.LocationDetailsView
import ru.limeek.organizer.locations.presenter.LocationPresenter
import ru.limeek.organizer.locations.presenter.LocationPresenterImpl
import ru.limeek.organizer.locations.view.LocationView
import ru.limeek.organizer.mvp.View

@Module
class PresenterModule(val view: View) {
    @Provides
    @ViewScope
    fun provideCalendarPresenter() : CalendarPresenter{
        return CalendarPresenterImpl(view as AppCalendarView)
    }

    @Provides
    @ViewScope
    fun provideEventsPresenter() : EventsPresenter{
        return EventsPresenterImpl(view as EventFragmentView)
    }

    @Provides
    @ViewScope
    fun provideEventsDetailsPresenter() : EventDetailsPresenter{
        return EventDetailsPresenterImpl(view as EventDetailsView)
    }

    @Provides
    @ViewScope
    fun provideLocationPresenter() : LocationPresenter{
        return LocationPresenterImpl(view as LocationView)
    }

    @Provides
    @ViewScope
    fun provideLocationDetailsPresenter() : LocationDetailsPresenter{
        return LocationDetailsPresenterImpl(view as LocationDetailsView)
    }
}