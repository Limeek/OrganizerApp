package ru.limeek.organizer.presenters

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import com.google.android.gms.location.places.Place
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import ru.limeek.organizer.R
import ru.limeek.organizer.app.App
import ru.limeek.organizer.di.modules.RepositoryModule
import ru.limeek.organizer.views.EventDetailsView
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.model.event.RemindTime
import ru.limeek.organizer.data.model.location.Location
import ru.limeek.organizer.data.model.location.LocationSpinnerItem
import ru.limeek.organizer.receiver.BroadcastReceiverNotification
import ru.limeek.organizer.data.repository.EventRepository
import ru.limeek.organizer.data.repository.LocationRepository
import ru.limeek.organizer.data.repository.SharedPrefsRepository
import ru.limeek.organizer.util.Constants
import javax.inject.Inject

class EventDetailsPresenterImpl(var eventDetailsView: EventDetailsView) : EventDetailsPresenter {
    val logTag = "EventDetailsPresenter"
    override var event: Event? = null
    var eventId: Long? = null
    var remindTime: RemindTime? = null
    override var remindTimeList: MutableList<RemindTime>? = RemindTime.values().toMutableList()
    var createdCustomLocation: Location? = null
    var chosenLocationId: Long? = null
    private var compositeDisposable: CompositeDisposable? = CompositeDisposable()

    @Inject
    lateinit var eventRepository: EventRepository
    @Inject
    lateinit var sharedPrefsRepository: SharedPrefsRepository
    @Inject
    lateinit var locationRepository: LocationRepository

    init {
        App.instance.component.newPresenterComponent(RepositoryModule()).inject(this)
    }

    override fun submit() {
        if (eventDetailsView.getTime() != "") {
            sharedPrefsRepository.putDateTime("cachedDate", DateTime.parse(eventDetailsView.getDate(), DateTimeFormat.forPattern(Constants.FORMAT_DD_MM_YYYY)))
            compositeDisposable!!.add(
                    Observable.fromCallable {
                        if (event != null) {
                            if (!eventDetailsView.isNotificationChecked())
                                remindTime = RemindTime.NOREMIND
                            if (eventDetailsView.isLocationChecked()) {
                                chosenLocationId = null
                                createdCustomLocation = null
                            }
                            updateEvent()
                        } else {
                            if (!eventDetailsView.isNotificationChecked()) remindTime = RemindTime.NOREMIND
                            insertEvent()
                            event!!.id = eventId!!
                        }
                    }
                            .concatWith(sendToAlarmManager())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe()
            )
        } else eventDetailsView.showErrorNotificationAndHideLayout()
    }

    override fun delete() {
        Observable.fromCallable {
            eventRepository.deleteEvent(event!!)
        }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    override fun onCreate() {
        event = eventDetailsView.getEvent()
        eventId = eventDetailsView.getEventId()
        when {
            event != null -> updateUI()
            eventId != null -> compositeDisposable!!.add(
                    eventRepository
                            .getEventById(eventId!!)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe { event ->
                                this.event = event
                                updateUI()
                            }
            )
            else -> eventDetailsView.updateDate(sharedPrefsRepository.getDateString("cachedDate"))
        }
    }

    override fun updateUI() {
        eventDetailsView.updateDate(event?.getDate()?: "")
        eventDetailsView.updateTime(event?.getTime()?: "")
        eventDetailsView.updateSummary(event?.summary?: "")
        if (event?.location != null) {
            if (eventDetailsView.getUneditable() == null) {
                eventDetailsView.updateLocationChooseVisibility(true)
                eventDetailsView.updateLocationSwitch(true)
                if (!event!!.location!!.createdByUser)
                    eventDetailsView.updateLocationAddress(event!!.location!!.address)
            } else {
                eventDetailsView.updateLocationCreationVisibility(true)
                if (!event!!.location!!.createdByUser)
                    eventDetailsView.updateLocationAddress(event!!.location!!.address)
                else
                    eventDetailsView.updateLocationAddress(event!!.location!!.name)
            }
        }

        updateSpinnerItems()

        if (event?.remind != RemindTime.NOREMIND && event?.dateTime!!.millis > DateTime.now().millis) {
            eventDetailsView.updateNotification(true) 
//            eventDetailsView.notificationLayout.visibility = View.VISIBLE
            eventDetailsView.setRemindSpinnerSelection(event!!.remind.ordinal - 1)
        }
    }

    override fun sendToAlarmManager(): Observable<Unit> {
        return Observable.fromCallable {
            if (getDateTimeFromEditTexts() > DateTime.now() && remindTime != RemindTime.NOREMIND) {
                val intent = Intent(App.instance.applicationContext, BroadcastReceiverNotification::class.java)

                intent.putExtra("eventId", event?.id)
                intent.putExtra("content", event?.summary)
                intent.putExtra("when", event?.dateTime?.millis)

                val pendingIntent = PendingIntent.getBroadcast(App.instance.applicationContext, event!!.id.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)

                App.instance.alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, event!!.dateTime.millis - remindTime!!.millis!!, pendingIntent)
            }
        }
    }

    override fun notificationSpinnerOnItemSelected(): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                remindTime = parent?.getItemAtPosition(position) as RemindTime
            }
        }
    }

    override fun updateSpinnerItems() {
        compositeDisposable!!.add(
                Observable.fromCallable {
                    val dateTime = getDateTimeFromEditTexts()
                    val newRemindTimeList = RemindTime.values().toMutableList()

                    newRemindTimeList.remove(RemindTime.NOREMIND)

                    val iterator = newRemindTimeList.listIterator()
                    iterator.forEach {
                        if (it.millis != null && dateTime.millis - it.millis <= DateTime.now().millis)
                            iterator.remove()
                    }
                    remindTimeList!!.clear()
                    remindTimeList!!.addAll(newRemindTimeList)
                    eventDetailsView.notifyNotificationAdapter()
                }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {},
                                { eventDetailsView.showErrorNotificationAndHideLayout() },
                                { Log.wtf("UpdateSpinnerObservable", "Completed") }
                        )
        )
    }

    override fun getDateTimeFromEditTexts(): DateTime {
        return DateTime.parse("${eventDetailsView.getDate()}${eventDetailsView.getTime()}", DateTimeFormat.forPattern(Constants.FORMAT_DD_MM_YY_HH_MM)).toDateTimeISO()
    }

    override fun getBtnLocationOnClick() {

    }

    override fun createLocation(place: Place) {
        createdCustomLocation = if (event?.location != null && !event?.location!!.createdByUser) {
            Location(event!!.location!!.id, place.latLng.longitude, place.latLng.latitude, place.name.toString(), place.address.toString(), false)
        } else
            Location(place.latLng.longitude, place.latLng.latitude, place.name.toString(), place.address.toString(), false)
    }

    override fun getMapUri(): String {
        return "geo:${event!!.location!!.latitude},${event!!.location!!.longitude}?q=" +
                "${event!!.location!!.latitude},${event!!.location!!.longitude}(${event!!.location!!.address})"
    }

    override fun setupLocationSpinner() {
        compositeDisposable!!.add(
                locationRepository.getUserCreatedLocations()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe { locations ->
                            val locationsSpinner = mutableListOf<LocationSpinnerItem>()
                            locationsSpinner.add(LocationSpinnerItem(App.instance.getString(R.string.choose), null))
                            for (location: Location in locations) {
                                locationsSpinner.add(LocationSpinnerItem(location.name, location.id))
                            }
                            locationsSpinner.add(LocationSpinnerItem(App.instance.getString(R.string.custom_location), null))
                            locationsSpinner.add(LocationSpinnerItem(App.instance.getString(R.string.new_location), null))
                            eventDetailsView.setupPlaceSpinnerAdapter(locationsSpinner)
                            when {
                                chosenLocationId != null -> {
                                    for (locationSpinnerItem: LocationSpinnerItem in locationsSpinner)
                                        if (locationSpinnerItem.locationId == chosenLocationId)
                                            eventDetailsView.setLocationSpinnerSelecetion(locationsSpinner.indexOf(locationSpinnerItem))
                                }
                                event != null && event!!.location != null && event!!.location!!.createdByUser -> {
                                    for (locationSpinnerItem: LocationSpinnerItem in locationsSpinner)
                                        if (locationSpinnerItem.locationId == event!!.location!!.id)
                                            eventDetailsView.setLocationSpinnerSelecetion(locationsSpinner.indexOf(locationSpinnerItem))
                                }
                                event != null && event!!.location != null && !event!!.location!!.createdByUser ->
                                    eventDetailsView.setLocationSpinnerSelecetion(locationsSpinner.size - 2)
                                else -> eventDetailsView.setLocationSpinnerSelecetion(0)
                            }
                        }
        )
    }

    override fun locationSpinnerOnItemsSelected(): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when {
                    (parent!!.getItemAtPosition(position) as LocationSpinnerItem).name == App.instance.getString(R.string.custom_location) &&
                            ((event?.location == null || event?.location!!.createdByUser) && eventDetailsView.getLocation() == "") -> {
                        eventDetailsView.updateLocationCreationVisibility(true)
                        chosenLocationId = null
                        eventDetailsView.startPlacePicker()
                    }
                    //If Custom location is already selected
                    (parent.getItemAtPosition(position) as LocationSpinnerItem).name == App.instance.getString(R.string.custom_location) -> {
                        eventDetailsView.updateLocationCreationVisibility(true)
                        chosenLocationId = null
                    }
                    (parent.getItemAtPosition(position) as LocationSpinnerItem).name == App.instance.getString(R.string.new_location) -> {
                        eventDetailsView.startLocationDetailsActivity()
                    }
                    else -> {
                        chosenLocationId = (parent.getItemAtPosition(position) as LocationSpinnerItem).locationId
                        eventDetailsView.updateLocationCreationVisibility(false)
                        createdCustomLocation = null
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    override fun onDestroy() {
        if (compositeDisposable != null) {
            compositeDisposable!!.dispose()
            compositeDisposable = null
        }
    }

    private fun updateEvent() {
        val updatedEvent: Event
        when {
            //NoLocInteraction
            event!!.location == null && createdCustomLocation == null && chosenLocationId == null -> {
                updatedEvent = Event(event!!.id, getDateTimeFromEditTexts(), eventDetailsView.getSummary(), remindTime!!)
                eventRepository.update(updatedEvent)
            }
            //UpdatingInUneditableWithLoc
            event!!.location != null && eventDetailsView.getUneditable() != null -> {
                updatedEvent = Event(event!!.id, getDateTimeFromEditTexts(), eventDetailsView.getSummary(), remindTime!!, event!!.locationId!!)
                eventRepository.update(updatedEvent)
            }
            //AddCustLoc
            event!!.location == null && createdCustomLocation != null -> {
                updatedEvent = Event(event!!.id, getDateTimeFromEditTexts(), eventDetailsView.getSummary(), remindTime!!, createdCustomLocation)
                eventRepository.updateEventWithAddedLocation(updatedEvent)
            }
            //AddUserLoc
            event!!.location == null && chosenLocationId != null -> {
                updatedEvent = Event(event!!.id, getDateTimeFromEditTexts(), eventDetailsView.getSummary(), remindTime!!, chosenLocationId!!)
                eventRepository.updateEventWithAddedLocation(updatedEvent)
            }
            //UserLoc To CustomLoc
            event!!.location != null && event!!.location!!.createdByUser && createdCustomLocation != null -> {
                updatedEvent = Event(event!!.id, getDateTimeFromEditTexts(), eventDetailsView.getSummary(), remindTime!!, createdCustomLocation)
                eventRepository.updateEventWithUserToCustomLoc(updatedEvent)
            }
            //CustomLoc To UserLoc
            event!!.location != null && !event!!.location!!.createdByUser && chosenLocationId != null -> {
                updatedEvent = Event(event!!.id, getDateTimeFromEditTexts(), eventDetailsView.getSummary(), remindTime!!, event!!.location!!, chosenLocationId!!)
                eventRepository.updateEventWithCustomToUserLoc(updatedEvent)
            }
            //CustomLoc To CustomLoc
            event!!.location != null && !event!!.location!!.createdByUser && createdCustomLocation != null -> {
                updatedEvent = Event(event!!.id, getDateTimeFromEditTexts(), eventDetailsView.getSummary(), remindTime!!, createdCustomLocation)
                eventRepository.updateEventWithCustomToCustomLoc(updatedEvent)
            }
            //UserLoc To UserLoc
            event!!.location != null && event!!.location!!.createdByUser && chosenLocationId != null -> {
                updatedEvent = Event(event!!.id, getDateTimeFromEditTexts(), eventDetailsView.getSummary(), remindTime!!, chosenLocationId!!)
                eventRepository.update(updatedEvent)
            }
            //DeletionOfUserLoc
            event!!.location != null && event!!.location!!.createdByUser && createdCustomLocation == null && chosenLocationId == null -> {
                updatedEvent = Event(event!!.id, getDateTimeFromEditTexts(), eventDetailsView.getSummary(), remindTime!!, event!!.location)
                eventRepository.updateEventWithUserLocDelete(updatedEvent)
            }
            //DeletionOfCustomLoc
            event!!.location != null && !event!!.location!!.createdByUser && createdCustomLocation == null && chosenLocationId == null -> {
                updatedEvent = Event(event!!.id, getDateTimeFromEditTexts(), eventDetailsView.getSummary(), remindTime!!, event!!.location)
                eventRepository.updateEventWithCustLocDelete(updatedEvent)
            }
        }
    }

    private fun insertEvent() {
        event = when {
            chosenLocationId != null -> Event(getDateTimeFromEditTexts(), eventDetailsView.getSummary(), remindTime!!, chosenLocationId!!)
            createdCustomLocation != null -> Event(getDateTimeFromEditTexts(), eventDetailsView.getSummary(), remindTime!!, createdCustomLocation!!)
            else -> Event(getDateTimeFromEditTexts(), eventDetailsView.getSummary(), remindTime!!)
        }
        eventId = eventRepository.insertEvent(event!!)
    }

    override fun onLocationDetailsResult(locationId: Long) {
        this.chosenLocationId = locationId
        setupLocationSpinner()
    }

}