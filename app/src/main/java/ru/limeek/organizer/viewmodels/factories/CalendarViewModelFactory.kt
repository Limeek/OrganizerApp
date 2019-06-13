package ru.limeek.organizer.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.viewmodels.CalendarViewModel
import javax.inject.Inject

class CalendarViewModelFactory @Inject constructor(private var sharedPrefRepo: SharedPrefsRepository): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CalendarViewModel::class.java)){
            return CalendarViewModel(sharedPrefRepo) as T
        }
        throw IllegalStateException()
    }
}