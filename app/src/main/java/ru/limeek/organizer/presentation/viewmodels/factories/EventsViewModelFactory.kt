package ru.limeek.organizer.presentation.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.domain.usecases.GetEventsByCachedDateUseCase
import ru.limeek.organizer.presentation.viewmodels.EventsViewModel
import javax.inject.Inject

class EventsViewModelFactory @Inject constructor(private val sharedPrefRepo: SharedPrefsRepository,
                                                 private val getEventsByCachedDateUseCase: GetEventsByCachedDateUseCase): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EventsViewModel::class.java)){
            return EventsViewModel(sharedPrefRepo, getEventsByCachedDateUseCase) as T
        }
        throw IllegalStateException()
    }
}