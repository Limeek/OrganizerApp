package ru.limeek.organizer.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.data.repository.LocationRepository
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.viewmodels.EventDetailsViewModel
import javax.inject.Inject

class EventDetailsViewModelFactory @Inject constructor(private var sharedPrefsRepo: SharedPrefsRepository,
                                                       private var eventRepository: EventRepository,
                                                       private var locationRepository: LocationRepository): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EventDetailsViewModel::class.java)){
            return EventDetailsViewModel(sharedPrefsRepo, eventRepository, locationRepository) as T
        }
        throw IllegalStateException()
    }
}