package ru.limeek.organizer.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.limeek.organizer.data.repository.LocationRepository
import ru.limeek.organizer.viewmodels.LocationDetailsViewModel
import javax.inject.Inject

class LocationDetailsViewModelFactory @Inject constructor(private val locationRepository: LocationRepository): ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LocationDetailsViewModel::class.java)) {
            return LocationDetailsViewModel(locationRepository) as T
        }
        throw IllegalStateException()
    }
}