package ru.limeek.organizer.event.presenter

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.limeek.organizer.app.App
import ru.limeek.organizer.di.modules.RepositoryModule
import ru.limeek.organizer.event.view.EventsListAdapter
import ru.limeek.organizer.model.Event.Event
import ru.limeek.organizer.model.repository.Repository
import javax.inject.Inject

class EventsAdapterPresenter (var eventsListAdapter: EventsListAdapter?) {
    val logTag = "EventsAdapterPresenter"
    var disposable : Disposable? = null
    var events : List<Event> = listOf()

    @Inject
    lateinit var repository: Repository

    init {
        App.instance.component.newPresenterComponent(RepositoryModule()).inject(this)
        updateEvents()
    }

    fun updateEvents(){
        disposable = repository.database.eventDao().getEventsByDate(repository.sharedPreferences.getDateTime("cachedDate"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { events ->
                    this.events = events
                    eventsListAdapter!!.notifyDataSetChanged()
                    Log.wtf(logTag, this.events.toString())
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