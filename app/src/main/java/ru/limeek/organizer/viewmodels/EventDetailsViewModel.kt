package ru.limeek.organizer.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.model.event.RemindTime
import ru.limeek.organizer.data.model.location.Location
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.usecases.DeleteEventUseCase
import ru.limeek.organizer.usecases.GetUserCreatedLocationsUseCase
import ru.limeek.organizer.usecases.InsertEventUseCase
import ru.limeek.organizer.usecases.UpdateEventUseCase
import ru.limeek.organizer.util.Constants
import ru.limeek.organizer.util.SingleLiveEvent

class EventDetailsViewModel(private val sharedPrefsRepo: SharedPrefsRepository,
                            private val insertEventUseCase: InsertEventUseCase,
                            private val updateEventUseCase: UpdateEventUseCase,
                            private val deleteEventUseCase: DeleteEventUseCase,
                            private val getUserCreatedLocationsUseCase: GetUserCreatedLocationsUseCase) : ViewModel() {

    private var event = MutableLiveData<Event>()
    private var oldEvent: Event? = null

    var eventId: Int = 0
        private set

    var date = Transformations.map(event) { event -> event.getDate() }
    var time = Transformations.map(event) { event -> event.getTime() }
    var summary = Transformations.map(event) { event -> event.summary }
    var remindTime = Transformations.map(event) { event -> event.remind }
    var location = Transformations.map(event) { event -> event.location }
    val remindTimeList = SingleLiveEvent<List<RemindTime>>()
    val userLocationList = SingleLiveEvent<List<Location>>()
    val finish = SingleLiveEvent<Boolean>()

    fun init(event: Event) {
        this.event.value = event
        initSpinnerItems()
        oldEvent = event.copy()
    }

    fun init() {
        event.value = Event()
        event.value?.dateTime = sharedPrefsRepo.getDateTime(Constants.CACHED_DATE)
        initSpinnerItems()
    }


    fun submitEvent() {
        when (oldEvent) {
            event.value!! -> finish.value = true
            null -> insertEvent()
            else -> updateEvent()
        }
    }

    fun deleteEvent() {
        removeEvent()
    }

    fun updateEventRemindTime(position: Int) {
        event.value!!.remind = remindTimeList.value?.get(position)!!
    }

    fun updateEventLocation(position: Int) {
        event.value!!.location = userLocationList.value?.get(position - 1)
    }

    fun updateEventLocation(location: Location) {
        event.value = event.value?.copy().apply { this?.location = location }
    }

    fun updateDate(date: String) {
        if (time.value == "")
            event.value = event.value?.copy(dateTime = DateTime.parse(date, DateTimeFormat.forPattern(Constants.FORMAT_DD_MM_YYYY)))
        else
            event.value = event.value?.copy(dateTime = DateTime.parse("$date${time.value}", DateTimeFormat.forPattern(Constants.FORMAT_DD_MM_YY_HH_MM)))
    }

    fun updateTime(time: String) {
        if (date.value == "")
            event.value = event.value?.copy(dateTime = DateTime.parse(time, DateTimeFormat.forPattern(Constants.FORMAT_HH_mm)))
        else
            event.value = event.value?.copy(dateTime = DateTime.parse("${date.value}$time", DateTimeFormat.forPattern(Constants.FORMAT_DD_MM_YY_HH_MM)))
    }

    fun updateSummary(summary: String) {
        event.value?.summary = summary
    }

    fun initLocationSpinnerItems() {
        viewModelScope.launch {
            userLocationList.value = getUserLocations()
        }
    }

    private fun initSpinnerItems() {
        viewModelScope.launch {
            val neededRemindTimes = RemindTime.values().toList().drop(1)
            remindTimeList.value = neededRemindTimes
            userLocationList.value = getUserLocations()
        }
    }

    private fun insertEvent() {
        viewModelScope.launch {
            insertEventUseCase.execute(event.value!!)
            finish.callAsync()
        }
    }

    private fun updateEvent() {
        viewModelScope.launch {
            updateEventUseCase.execute(event.value!!)
            finish.callAsync()
        }
    }

    private fun removeEvent() {
        viewModelScope.launch {
            deleteEventUseCase.execute(event.value!!)
            finish.callAsync()
        }
    }

    private suspend fun getUserLocations(): List<Location> {
        return getUserCreatedLocationsUseCase.execute()
    }
}