package ru.limeek.organizer.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.limeek.organizer.data.repository.LocationRepository
import ru.limeek.organizer.viewmodels.LocationViewModel
import javax.inject.Inject

class LocationViewModelFactory @Inject constructor(private var locationRepository: LocationRepository): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LocationViewModel::class.java)){
            return LocationViewModel(locationRepository) as T
        }
        throw IllegalStateException()
    }
}