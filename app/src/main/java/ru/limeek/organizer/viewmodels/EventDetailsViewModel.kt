package ru.limeek.organizer.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.model.event.RemindTime
import ru.limeek.organizer.data.model.location.Location
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.data.repository.LocationRepository
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.util.Constants
import ru.limeek.organizer.util.SingleLiveEvent

class EventDetailsViewModel(private var sharedPresRepo: SharedPrefsRepository,
                            private var eventRepo: EventRepository,
                            private var locationRepo: LocationRepository) : ViewModel(){

    private var event = MutableLiveData<Event>()
    private var oldEvent: Event? = null

    var eventId: Int = 0
        private set

    var date = Transformations.map(event) { event -> event.getDate() }
    var time = Transformations.map(event) { event -> event.getTime() }
    var summary = Transformations.map(event) { event -> event.summary }
    var remindTime = Transformations.map(event) {event -> event.remind }
    var location = Transformations.map(event) {event -> event.location }
    val remindTimeList = SingleLiveEvent<List<RemindTime>>()
    val userLocationList = SingleLiveEvent<List<Location>>()
    val finish = SingleLiveEvent<Boolean>()

    fun init(event: Event){
        this.event.value = event
        initSpinnerItems()
        oldEvent = event
    }

    fun init(){
        event.value = Event()
        event.value?.dateTime = sharedPresRepo.getDateTime(Constants.CACHED_DATE)
        initSpinnerItems()
    }

    fun submitEvent(){
        when (oldEvent) {
            event.value!! -> finish.value = true
            null -> insertEvent()
            else -> updateEvent()
        }
    }

    fun deleteEvent(){
         removeEvent()
    }

    fun updateEventRemindTime(position: Int){
        event.value?.remind = remindTimeList.value?.get(position)!!
    }

    fun updateEventLocation(position: Int){
        event.value?.location = userLocationList.value?.get(position)
    }

    fun updateDate(date: String){
        if(time.value == "")
            event.value?.dateTime = DateTime.parse(date, DateTimeFormat.forPattern(Constants.FORMAT_DD_MM_YYYY))
        else
            event.value?.dateTime = DateTime.parse("$date${time.value}", DateTimeFormat.forPattern(Constants.FORMAT_DD_MM_YY_HH_MM))
    }

    fun updateTime(time: String){
        if(date.value == "")
            event.value?.dateTime = DateTime.parse(time, DateTimeFormat.forPattern(Constants.FORMAT_HH_mm))
        else
            event.value?.dateTime = DateTime.parse("${date.value}$time", DateTimeFormat.forPattern(Constants.FORMAT_DD_MM_YY_HH_MM))
    }

    fun updateSummary(summary: String){
        event.value?.summary = summary
    }

    private fun initSpinnerItems(){
        viewModelScope.launch {
            remindTimeList.value = RemindTime.values().toList()
            userLocationList.value = getUserLocations()
        }
    }

    private fun getEventById(){
        viewModelScope.launch {
            event.value = eventRepo.getEventById(eventId.toLong())
        }
    }

    private fun insertEvent(){
        viewModelScope.launch {
            eventRepo.insert(event.value!!)
        }
    }

    private fun updateEvent(){
        viewModelScope.launch {
            eventRepo.update(event.value!!)
        }
    }

    private fun removeEvent(){
        viewModelScope.launch {
            eventRepo.delete(event.value!!)
        }
    }

    private suspend fun getUserLocations(): List<Location>{
        return locationRepo.getUserCreatedLocations()
    }
}