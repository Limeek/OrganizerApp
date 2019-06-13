package ru.limeek.organizer.di.modules

import dagger.Module
import dagger.Provides
import ru.limeek.organizer.di.scopes.ViewScope
import ru.limeek.organizer.presenters.EventDetailsPresenter
import ru.limeek.organizer.presenters.EventDetailsPresenterImpl
import ru.limeek.organizer.presenters.LocationDetailsPresenter
import ru.limeek.organizer.presenters.LocationDetailsPresenterImpl
import ru.limeek.organizer.presenters.LocationPresenter
import ru.limeek.organizer.presenters.LocationPresenterImpl
import ru.limeek.organizer.views.*

@Module
class PresenterModule(val view: View) {
//    @Provides
//    @ViewScope
//    fun provideCalendarPresenter() : CalendarPresenter {
//        return CalendarPresenterImpl(view as CalendarFragment)
//    }

//    @Provides
//    @ViewScope
//    fun provideEventsPresenter() : EventsPresenter {
//        return EventsPresenterImpl(view as EventFragmentView)
//    }

    @Provides
    @ViewScope
    fun provideEventsDetailsPresenter() : EventDetailsPresenter {
        return EventDetailsPresenterImpl(view as EventDetailsView)
    }

    @Provides
    @ViewScope
    fun provideLocationPresenter() : LocationPresenter {
        return LocationPresenterImpl(view as LocationView)
    }

    @Provides
    @ViewScope
    fun provideLocationDetailsPresenter() : LocationDetailsPresenter {
        return LocationDetailsPresenterImpl(view as LocationDetailsView)
    }
}