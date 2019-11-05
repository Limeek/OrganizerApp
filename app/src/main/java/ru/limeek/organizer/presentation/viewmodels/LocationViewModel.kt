package ru.limeek.organizer.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.limeek.organizer.domain.entities.location.Location
import ru.limeek.organizer.domain.usecases.GetUserCreatedLocationsUseCase
import ru.limeek.organizer.presentation.util.SingleLiveEvent
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LocationViewModel @Inject constructor(private val getUserCreatedLocationsUseCase: GetUserCreatedLocationsUseCase): ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.IO + SupervisorJob()

    val locations = MutableLiveData<List<Location>>()
    val startDetailsActivity = SingleLiveEvent<Unit>()

    init {
        getLocations()
    }

    fun onFabClick(){
        startDetailsActivity.call()
    }

    private fun getLocations(){
        launch {
            locations.postValue(getUserCreatedLocationsUseCase.execute())
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineContext[Job]!!.cancel()
    }
}