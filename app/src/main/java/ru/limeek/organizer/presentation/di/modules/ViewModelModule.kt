package ru.limeek.organizer.presentation.di.modules

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.data.repository.LocationRepository
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.domain.usecases.*
import ru.limeek.organizer.presentation.viewmodels.*
import ru.limeek.organizer.presentation.viewmodels.factories.CalendarViewModelFactory
import ru.limeek.organizer.presentation.viewmodels.factories.EventDetailsViewModelFactory
import ru.limeek.organizer.presentation.viewmodels.factories.EventsViewModelFactory
import ru.limeek.organizer.presentation.viewmodels.factories.LocationDetailsViewModelFactory

@Module
open class ViewModelModule() {

    var activity: FragmentActivity? = null
    var fragment: Fragment? = null

    constructor(activity: FragmentActivity) : this() {
        this.activity = activity
    }

    constructor(fragment: Fragment) : this() {
        this.fragment = fragment
    }

    @Provides
    open fun provideMainViewModel(): MainViewModel {
        return ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    @Provides
    open fun provideCalendarViewModel(sharedPrefsRepo: SharedPrefsRepository): CalendarViewModel {
        return ViewModelProviders.of(fragment!!, CalendarViewModelFactory(sharedPrefsRepo)).get(CalendarViewModel::class.java)
    }

    @Provides
    open fun provideEventsViewModel(sharedPrefsRepo: SharedPrefsRepository,
                               getEventsByCachedDateUseCase: GetEventsByCachedDateUseCase): EventsViewModel {
        return ViewModelProviders.of(fragment!!, EventsViewModelFactory(sharedPrefsRepo, getEventsByCachedDateUseCase)).get(EventsViewModel::class.java)
    }

    @Provides
    open fun provideEventDetailsViewModel(sharedPrefsRepo: SharedPrefsRepository,
                                     insertEventUseCase: InsertEventUseCase,
                                     updateEventUseCase: UpdateEventUseCase,
                                     deleteEventUseCase: DeleteEventUseCase,
                                     getUserCreatedLocationsUseCase: GetUserCreatedLocationsUseCase
    ): EventDetailsViewModel {
        return ViewModelProviders.of(activity!!, EventDetailsViewModelFactory(sharedPrefsRepo,
                insertEventUseCase,
                updateEventUseCase,
                deleteEventUseCase,
                getUserCreatedLocationsUseCase))
                .get(EventDetailsViewModel::class.java)
    }

    @Provides
    open fun provideLocationDetailsViewModel(insertLocationUseCase: InsertLocationUseCase,
                                        updateLocationUseCase: UpdateLocationUseCase,
                                        deleteLocationUseCase: DeleteLocationUseCase): LocationDetailsViewModel {
        return ViewModelProviders.of(activity!!, LocationDetailsViewModelFactory(insertLocationUseCase,
                updateLocationUseCase,
                deleteLocationUseCase)).get(LocationDetailsViewModel::class.java)
    }
}