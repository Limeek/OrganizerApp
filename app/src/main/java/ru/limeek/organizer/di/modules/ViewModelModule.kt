package ru.limeek.organizer.di.modules

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.data.repository.LocationRepository
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.viewmodels.*
import ru.limeek.organizer.viewmodels.factories.CalendarViewModelFactory
import ru.limeek.organizer.viewmodels.factories.EventDetailsViewModelFactory
import ru.limeek.organizer.viewmodels.factories.EventsViewModelFactory
import ru.limeek.organizer.viewmodels.factories.LocationDetailsViewModelFactory

@Module
class ViewModelModule() {

    var activity: FragmentActivity? = null
    var fragment: Fragment? = null

    constructor(activity: FragmentActivity): this(){
        this.activity = activity
    }

    constructor(fragment: Fragment): this(){
        this.fragment = fragment
    }

    @Provides
    fun provideMainViewModel(): MainViewModel{
        return ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    @Provides
    fun provideCalendarViewModel(sharedPrefsRepo: SharedPrefsRepository): CalendarViewModel {
        return ViewModelProviders.of(fragment!!, CalendarViewModelFactory(sharedPrefsRepo)).get(CalendarViewModel::class.java)
    }

    @Provides
    fun provideEventsViewModel(sharedPrefsRepo: SharedPrefsRepository,
                               eventsRepo: EventRepository): EventsViewModel{
        return ViewModelProviders.of(fragment!!, EventsViewModelFactory(sharedPrefsRepo, eventsRepo)).get(EventsViewModel::class.java)
    }

    @Provides
    fun provideEventDetailsViewModel(sharedPrefsRepo: SharedPrefsRepository,
                                     eventsRepo: EventRepository,
                                     locationRepository: LocationRepository): EventDetailsViewModel {
        return ViewModelProviders.of(activity!!, EventDetailsViewModelFactory(sharedPrefsRepo, eventsRepo, locationRepository))
                .get(EventDetailsViewModel::class.java)
    }

    @Provides
    fun provideLocationDetailsViewModel(locationRepository: LocationRepository): LocationDetailsViewModel{
        return ViewModelProviders.of(activity!!, LocationDetailsViewModelFactory(locationRepository)).get(LocationDetailsViewModel::class.java)
    }
}