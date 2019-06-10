package ru.limeek.organizer.presenters

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.limeek.organizer.app.App
import ru.limeek.organizer.di.modules.RepositoryModule
import ru.limeek.organizer.views.EventsListAdapter
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import javax.inject.Inject

class EventsAdapterPresenter (var eventsListAdapter: EventsListAdapter?) {
    val logTag = "EventsAdapterPresenter"
    var disposable : Disposable? = null
    var events : List<Event> = listOf()

    @Inject
    lateinit var eventRepository: EventRepository

    @Inject
    lateinit var sharedPrefsRepository: SharedPrefsRepository

    init {
        App.instance.component.newPresenterComponent(RepositoryModule()).inject(this)
        updateEvents()
    }

    fun updateEvents(){
        disposable = eventRepository.getEventsByDate(sharedPrefsRepository.getDateTime("cachedDate"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { events ->
                    this.events = events
                    eventsListAdapter!!.notifyDataSetChanged()
                }
    }

    fun getCount() = events.size

    fun getItemAt(position : Int) = events[position]

    fun onDestroy(){
        disposable!!.dispose()
        disposable = null
        eventsListAdapter = null
    }
}