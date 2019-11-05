package ru.limeek.organizer.presentation.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.limeek.organizer.data.repository.LocationRepository
import ru.limeek.organizer.domain.usecases.GetUserCreatedLocationsUseCase
import ru.limeek.organizer.presentation.viewmodels.LocationViewModel
import javax.inject.Inject

class LocationViewModelFactory @Inject constructor(private val getUserCreatedLocationsUseCase: GetUserCreatedLocationsUseCase): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LocationViewModel::class.java)){
            return LocationViewModel(getUserCreatedLocationsUseCase) as T
        }
        throw IllegalStateException()
    }
}