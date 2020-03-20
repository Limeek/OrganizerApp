package ru.limeek.organizer.presentation.viewmodels

import androidx.lifecycle.ViewModel
import ru.limeek.organizer.presentation.util.SingleLiveEvent

class MainViewModel: ViewModel(){
    val openEventDetailsFrag = SingleLiveEvent<Boolean>()

    fun onFabClick(){
        openEventDetailsFrag.call()
    }
}