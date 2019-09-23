package ru.limeek.organizer.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.limeek.organizer.data.model.location.Location
import ru.limeek.organizer.data.repository.LocationRepository
import ru.limeek.organizer.usecases.GetUserCreatedLocationsUseCase
import ru.limeek.organizer.util.SingleLiveEvent
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