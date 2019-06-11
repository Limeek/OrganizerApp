package ru.limeek.organizer.di.modules

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.di.scopes.PresenterScope
import ru.limeek.organizer.viewmodels.CalendarViewModel
import ru.limeek.organizer.viewmodels.EventsViewModel
import ru.limeek.organizer.viewmodels.MainViewModel
import ru.limeek.organizer.viewmodels.factories.CalendarViewModelFactory
import ru.limeek.organizer.viewmodels.factories.EventsViewModelFactory

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
    fun provideEvenetsViewModel(sharedPrefsRepo: SharedPrefsRepository): EventsViewModel{
        return  ViewModelProviders.of(fragment!!, EventsViewModelFactory(sharedPrefsRepo)).get(EventsViewModel::class.java)
    }
}