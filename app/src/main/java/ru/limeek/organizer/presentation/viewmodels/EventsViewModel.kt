package ru.limeek.organizer.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ru.limeek.organizer.domain.entities.event.Event
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.domain.usecases.GetEventsByCachedDateUseCase
import ru.limeek.organizer.presentation.util.Constants
import ru.limeek.organizer.presentation.util.SingleLiveEvent

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
