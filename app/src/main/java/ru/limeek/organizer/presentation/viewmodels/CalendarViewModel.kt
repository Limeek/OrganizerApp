package ru.limeek.organizer.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.presentation.util.Constants
import ru.limeek.organizer.presentation.util.SingleLiveEvent

class CalendarViewModel(private var repository: SharedPrefsRepository) : ViewModel() {
    val refreshEventFragment = SingleLiveEvent<Unit>()
    val currDate = MutableLiveData<Long>().apply { value = null }

    val onDateChange = fun(year: Int, month: Int, day: Int){
        val cachedDate = DateTime.parse("$year-${month + 1}-$day", DateTimeFormat.forPattern(Constants.FORMAT_YY_MM_dd))
        repository.putDateTime(Constants.CACHED_DATE, cachedDate)
        refreshEventFragment.call()
    }

    fun onCreate() {
        repository.putDateTime(Constants.CACHED_DATE, DateTime.now())
        currDate.value = repository.getDateTime(Constants.CACHED_DATE).millis
    }
}