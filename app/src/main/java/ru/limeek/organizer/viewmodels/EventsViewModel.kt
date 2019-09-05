package ru.limeek.organizer.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.util.Constants
import ru.limeek.organizer.util.SingleLiveEvent
import kotlin.coroutines.CoroutineContext

class EventsViewModel(private val sharedPrefsRepo: SharedPrefsRepository,
                      private val eventRepository: EventRepository) : ViewModel() {
    val logTag = "EventsPresenter"

    var events = MutableLiveData<List<Event>>()
    val startDetailsActivity = SingleLiveEvent<Unit>()
    val currentDateString = SingleLiveEvent<String>()

    fun onFabClick() {
        startDetailsActivity.call()
    }

    private fun updateEvents() = viewModelScope.launch {
        val dateEvents = eventRepository.getEventsByDate(sharedPrefsRepo.getDateTime(Constants.CACHED_DATE))
        events.postValue(dateEvents)
    }

    fun refresh(){
        currentDateString.value = sharedPrefsRepo.getDateString(Constants.CACHED_DATE)
        updateEvents()
    }
}
