package ru.limeek.organizer.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.util.SingleLiveEvent

class EventsViewModel(var sharedPrefsRepo: SharedPrefsRepository,
                      var eventRepository: EventRepository) : ViewModel() {
    val logTag = "EventsPresenter"

    var events = MutableLiveData<List<Event>>()

    val startDetailsActivity = SingleLiveEvent<Unit>()
    val notifyAdapter = SingleLiveEvent<Unit>()
    val currentDateString = SingleLiveEvent<String>()
    val compositeDisposable = CompositeDisposable()


    fun onFabClick(): View.OnClickListener {
        return View.OnClickListener {
            startDetailsActivity.call()
        }
    }

    fun getCurrentDateString(): String {
        return sharedPrefsRepo.getDateString("cachedDate")
    }

    fun updateEvents() {
        compositeDisposable.add(
                eventRepository.getEventsByDate(sharedPrefsRepo.getDateTime("cachedDate"))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe { events ->
                        this.events.value = events
                        notifyAdapter.call()
                    }
        )
    }

}
