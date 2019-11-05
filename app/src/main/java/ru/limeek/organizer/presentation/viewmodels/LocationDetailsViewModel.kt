package ru.limeek.organizer.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.limeek.organizer.domain.entities.location.Location
import ru.limeek.organizer.domain.usecases.DeleteLocationUseCase
import ru.limeek.organizer.domain.usecases.InsertLocationUseCase
import ru.limeek.organizer.domain.usecases.UpdateLocationUseCase
import ru.limeek.organizer.presentation.util.SingleLiveEvent

class LocationDetailsViewModel(private val insertLocationUseCase: InsertLocationUseCase,
                               private val updateLocationUseCase: UpdateLocationUseCase,
                               private val deleteLocationUseCase: DeleteLocationUseCase): ViewModel() {

    var finish = SingleLiveEvent<Location>()
    var location = MutableLiveData<Location>()
    var name = Transformations.map(location) { it.name }
    var address = Transformations.map(location) { it.address }

    private var startLocation: Location? = null

    fun init(location: Location?){
        if(location == null)
            this.location.value = Location()
        else
            this.location.value = location
    }

    fun updateName(name: String){
        location.value?.name = name
    }

    fun updateAddress(address: String){
        location.value?.address = address
    }

    fun delete() {
        viewModelScope.launch {
            deleteLocationUseCase.execute(location.value!!)
            finish.call()
        }
    }

    fun submit() {
        viewModelScope.launch {
            if(location.value == startLocation){
                return@launch
            }

            if(startLocation == null){
                location.value!!.createdByUser = true
                insertLocationUseCase.execute(location.value!!)
            }
            else
                updateLocationUseCase.execute(location.value!!)

            finish.value = location.value!!
        }
    }
}