package ru.limeek.organizer.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import ru.limeek.organizer.app.App
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.di.modules.RepositoryModule
import ru.limeek.organizer.util.Constants
import ru.limeek.organizer.util.SingleLiveEvent

class CalendarViewModel(private var repository: SharedPrefsRepository) : ViewModel() {
    private val logTag = "CalendarViewModel"

    val refreshEventFragment = SingleLiveEvent<Unit>()
    val currDate = MutableLiveData<Long>().apply { value = null }

    fun onDateChange(year: Int, month: Int, day: Int){
        val cachedDate = DateTime.parse("$year-${month + 1}-$day", DateTimeFormat.forPattern(Constants.FORMAT_YY_MM_dd))
        repository.putDateTime("cachedDate", cachedDate)
        refreshEventFragment.call()
    }

    fun onCreate() {
        currDate.value = repository.getDateTime("cachedDate").millis
    }
}