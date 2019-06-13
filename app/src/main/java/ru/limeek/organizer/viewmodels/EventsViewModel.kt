package ru.limeek.organizer.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.util.SingleLiveEvent
import kotlin.coroutines.CoroutineContext

class EventsViewModel(private var sharedPrefsRepo: SharedPrefsRepository,
                      private var eventRepository: EventRepository) : ViewModel(), CoroutineScope {
    val logTag = "EventsPresenter"

    override val coroutineContext: CoroutineContext = Dispatchers.IO + SupervisorJob()

    var events = MutableLiveData<List<Event>>()
    val startDetailsActivity = SingleLiveEvent<Unit>()
    val currentDateString = SingleLiveEvent<String>()

    fun onFabClick() {
        startDetailsActivity.call()
    }

    private fun updateEvents() = launch {
        val dateEvents = eventRepository.getEventsByDate(sharedPrefsRepo.getDateTime("cachedDate"))
        events.postValue(dateEvents)
    }

    fun refresh(){
        currentDateString.value = sharedPrefsRepo.getDateString("cachedDate")
        updateEvents()
    }

    override fun onCleared() {
        super.onCleared()
        coroutineContext[Job]?.cancel()
    }

}
