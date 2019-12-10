package ru.limeek.organizer.presentation.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.limeek.organizer.domain.usecases.DeleteLocationUseCase
import ru.limeek.organizer.domain.usecases.InsertLocationUseCase
import ru.limeek.organizer.domain.usecases.UpdateLocationUseCase
import ru.limeek.organizer.presentation.viewmodels.LocationDetailsViewModel
import javax.inject.Inject

class LocationDetailsViewModelFactory @Inject constructor(private val insertLocationUseCase: InsertLocationUseCase,
                                                          private val updateLocationUseCase: UpdateLocationUseCase,
                                                          private val deleteLocationUseCase: DeleteLocationUseCase): ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LocationDetailsViewModel::class.java)) {
            return LocationDetailsViewModel(insertLocationUseCase,
                    updateLocationUseCase,
                    deleteLocationUseCase) as T
        }
        throw IllegalStateException()
    }
}