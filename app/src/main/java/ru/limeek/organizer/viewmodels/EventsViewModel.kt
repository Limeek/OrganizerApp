package ru.limeek.organizer.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.usecases.GetEventsByCachedDateUseCase
import ru.limeek.organizer.usecases.GetEventsByDateUseCase
import ru.limeek.organizer.util.Constants
import ru.limeek.organizer.util.SingleLiveEvent
import kotlin.coroutines.CoroutineContext

class EventsViewModel(private val sharedPrefsRepo: SharedPrefsRepository,
                      private val getEventsByCachedDateUseCase: GetEventsByCachedDateUseCase) : ViewModel() {
    val logTag = "EventsPresenter"

    var events = MutableLiveData<List<Event>>()
    val currentDateString = SingleLiveEvent<String>()

    private fun updateEvents() = viewModelScope.launch {
        val dateEvents = getEventsByCachedDateUseCase.execute()
        events.postValue(dateEvents)
    }

    fun refresh(){
        currentDateString.value = sharedPrefsRepo.getDateString(Constants.CACHED_DATE)
        updateEvents()
    }
}
