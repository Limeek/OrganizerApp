package ru.limeek.organizer.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.viewmodels.EventsViewModel
import javax.inject.Inject

class EventsViewModelFactory @Inject constructor(private var sharedPrefRepo: SharedPrefsRepository,
                                                 private var eventsRepo: EventRepository): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EventsViewModel::class.java)){
            return EventsViewModel(sharedPrefRepo, eventsRepo) as T
        }
        throw IllegalStateException()
    }
}