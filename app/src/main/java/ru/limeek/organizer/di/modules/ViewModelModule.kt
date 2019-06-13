package ru.limeek.organizer.di.modules

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.data.repository.*
import ru.limeek.organizer.di.scopes.PresenterScope
import ru.limeek.organizer.viewmodels.*
import ru.limeek.organizer.viewmodels.factories.*

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
    @PresenterScope
    fun provideMainViewModel(): MainViewModel{
        return ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    @Provides
    @PresenterScope
    fun provideCalendarViewModel(sharedPrefsRepo: SharedPrefsRepository): CalendarViewModel {
        return ViewModelProviders.of(fragment!!, CalendarViewModelFactory(sharedPrefsRepo)).get(CalendarViewModel::class.java)
    }

    @Provides
    @PresenterScope
    fun provideEventsViewModel(sharedPrefsRepo: SharedPrefsRepository,
                               eventsRepo: EventRepository): EventsViewModel{
        return ViewModelProviders.of(fragment!!, EventsViewModelFactory(sharedPrefsRepo, eventsRepo)).get(EventsViewModel::class.java)
    }

    @Provides
    @PresenterScope
    fun provideEventDetailsViewModel(sharedPrefsRepo: SharedPrefsRepository,
                                     eventsRepo: EventRepository,
                                     locationRepository: LocationRepository): EventDetailsViewModel {
        return ViewModelProviders.of(activity!!, EventDetailsViewModelFactory(sharedPrefsRepo, eventsRepo, locationRepository))
                .get(EventDetailsViewModel::class.java)
    }

    @Provides
    @PresenterScope
    fun provideWeatherViewModel(weatherRepo: WeatherRepository): WeatherViewModel{
        return ViewModelProviders.of(fragment!!, WeatherViewModelFactory(weatherRepo)).get(WeatherViewModel::class.java)
    }

    @Provides
    @PresenterScope
    fun provideNewsViewModel(newsRepo: NewsRepository): NewsViewModel{
        return ViewModelProviders.of(fragment!!, NewsViewModelFactory(newsRepo)).get(NewsViewModel::class.java)
    }
}