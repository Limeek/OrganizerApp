package ru.limeek.organizer.presentation.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.domain.usecases.DeleteEventUseCase
import ru.limeek.organizer.domain.usecases.GetUserCreatedLocationsUseCase
import ru.limeek.organizer.domain.usecases.InsertEventUseCase
import ru.limeek.organizer.domain.usecases.UpdateEventUseCase
import ru.limeek.organizer.presentation.viewmodels.EventDetailsViewModel
import javax.inject.Inject

class EventDetailsViewModelFactory @Inject constructor(private val sharedPrefsRepo: SharedPrefsRepository,
                                                       private val insertEventUseCase: InsertEventUseCase,
                                                       private val updateEventUseCase: UpdateEventUseCase,
                                                       private val deleteEventUseCase: DeleteEventUseCase,
                                                       private val getUserCreatedLocationsUseCase: GetUserCreatedLocationsUseCase): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EventDetailsViewModel::class.java)){
            return EventDetailsViewModel(sharedPrefsRepo,
                    insertEventUseCase,
                    updateEventUseCase,
                    deleteEventUseCase,
                    getUserCreatedLocationsUseCase) as T
        }
        throw IllegalStateException()
    }
}