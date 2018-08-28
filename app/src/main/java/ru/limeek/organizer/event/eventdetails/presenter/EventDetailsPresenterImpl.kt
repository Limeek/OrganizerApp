package ru.limeek.organizer.event.eventdetails.presenter

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
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
import ru.limeek.organizer.event.eventdetails.view.DatePickerDialogFragment
import ru.limeek.organizer.event.eventdetails.view.EventDetailsView
import ru.limeek.organizer.event.eventdetails.view.TimePickerDialogFragment
import ru.limeek.organizer.model.Event.Event
import ru.limeek.organizer.model.Event.RemindTime
import ru.limeek.organizer.model.Location.Location
import ru.limeek.organizer.model.Location.LocationSpinnerItem
import ru.limeek.organizer.model.repository.Repository
import ru.limeek.organizer.receiver.BroadcastReceiverNotification
import ru.limeek.organizer.util.Constants
import javax.inject.Inject

class EventDetailsPresenterImpl (var eventDetailsView: EventDetailsView) : EventDetailsPresenter {

    val logTag = "EventDetailsPresenter"
    override var event : Event? = null
    var eventId : Long? = null
    var remindTime : RemindTime? = null
    override var remindTimeList : MutableList<RemindTime>? = RemindTime.values().toMutableList()
    var createdCustomLocation : Location? = null
    var chosenLocationId : Long? = null
    private var compositeDisposable: CompositeDisposable? = CompositeDisposable()
    
    @Inject
    lateinit var repository: Repository

    init {
        App.instance.component.newPresenterComponent(RepositoryModule()).inject(this)
    }

    override fun submit() {
        if(eventDetailsView.time.text.toString() != "") {
            repository.sharedPreferences.putDateTime("cachedDate", DateTime.parse(eventDetailsView.date.text.toString(), DateTimeFormat.forPattern(Constants.FORMAT_DD_MM_YYYY)))
            eventDetailsView.startCalendarActivity()
            compositeDisposable!!.add(
                    Observable.fromCallable {
                        if (event != null) {
                            if (!eventDetailsView.notification.isChecked)
                                remindTime = RemindTime.NOREMIND
                            if (eventDetailsView.locationSwitch != null && !eventDetailsView.locationSwitch!!.isChecked) {
                                chosenLocationId = null
                                createdCustomLocation = null
                            }
                            updateEvent()
                        } else {
                            if (!eventDetailsView.notification.isChecked) remindTime = RemindTime.NOREMIND
                            insertEvent()
                            event!!.id = eventId!!
                        }
                    }
                            .concatWith(sendToAlarmManager())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe()
            )
        }
        else eventDetailsView.showErrorNotificationAndHideLayout()
    }

    override fun delete() {
        Observable.fromCallable{
            repository.database.eventDao().deleteEvent(event!!)
        }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    override fun onCreate(){
        event = eventDetailsView.getEvent()
        eventId = eventDetailsView.getEventId()
        when {
            event != null -> updateUI()
            eventId != null -> compositeDisposable!!.add(
                    repository.database.eventDao()
                            .getEventById(eventId!!)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe { event ->
                                this.event = event
                                updateUI()
                            }
            )
            else -> eventDetailsView.date.setText(repository.sharedPreferences.getDateString("cachedDate"))
        }
    }

    override fun updateUI(){
        eventDetailsView.date.setText(event?.getDate())
        eventDetailsView.time.setText(event?.getTime())
        eventDetailsView.summary.setText(event?.summary)
        if(event?.location != null){
            if(eventDetailsView.getUneditable() == null) {
                eventDetailsView.locationChooseLayout.visibility = View.VISIBLE
                eventDetailsView.locationSwitch!!.isChecked = true
                if(!event!!.location!!.createdByUser)
                    eventDetailsView.locationAddress.setText(event!!.location!!.address)
            }
            else{
                eventDetailsView.locationCreationLayout.visibility = View.VISIBLE
                if(!event!!.location!!.createdByUser)
                    eventDetailsView.locationAddress.setText(event!!.location!!.address)
                else
                    eventDetailsView.locationAddress.setText(event!!.location!!.name)
            }
        }

        updateSpinnerItems()

        if(event?.remind != RemindTime.NOREMIND && event?.dateTime!!.millis > DateTime.now().millis){
            eventDetailsView.notification.isChecked = true
            eventDetailsView.notificationLayout.visibility = View.VISIBLE
            eventDetailsView.remind.setSelection(event!!.remind.ordinal -  1)
        }
    }


    override fun createDateDialog(): DatePickerDialogFragment {
        val datePickerDialogFragment = DatePickerDialogFragment()
        val bundle = Bundle()
        val dateTime = DateTime.parse(eventDetailsView.date.text.toString(),DateTimeFormat.forPattern(Constants.FORMAT_DD_MM_YYYY))
        bundle.putInt("day", dateTime.dayOfMonth)
        bundle.putInt("month", dateTime.monthOfYear - 1)
        bundle.putInt("year", dateTime.year)
        datePickerDialogFragment.arguments = bundle
        return datePickerDialogFragment
    }

    override fun createTimeDialog(): TimePickerDialogFragment {
        val timePickerDialogFragment = TimePickerDialogFragment()
        val bundle = Bundle()
        bundle.putInt("hour", DateTime.now().hourOfDay)
        bundle.putInt("minute", DateTime.now().minuteOfHour)
        timePickerDialogFragment.arguments = bundle
        return timePickerDialogFragment
    }

    override fun sendToAlarmManager() : Observable<Unit> {
        return Observable.fromCallable{
            if(getDateTimeFromEditTexts() > DateTime.now() && remindTime!= RemindTime.NOREMIND) {
                val intent = Intent(eventDetailsView.getContext(), BroadcastReceiverNotification::class.java)

                intent.putExtra("eventId", event?.id)
                intent.putExtra("content", event?.summary)
                intent.putExtra("when", event?.dateTime?.millis)

                val pendingIntent = PendingIntent.getBroadcast(eventDetailsView.getContext(), event!!.id.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)

                App.instance.alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, event!!.dateTime.millis - remindTime!!.millis!!, pendingIntent)
            }
        }
    }

    override fun notificationSpinnerOnItemSelected() : AdapterView.OnItemSelectedListener{
        return object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                remindTime = parent?.getItemAtPosition(position) as RemindTime
            }
        }
    }

    override fun updateSpinnerItems(){
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
                eventDetailsView.notificationAdapter.notifyDataSetChanged()
            }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe (
                        {},
                        {
                            _ ->
                            eventDetailsView.showErrorNotificationAndHideLayout()
                        },
                        { Log.wtf("UpdateSpinnerObservable","Completed")}
                    )
        )
    }

    override fun getDateTimeFromEditTexts() : DateTime{
        return DateTime.parse("${eventDetailsView.date.text}${eventDetailsView.time.text}",DateTimeFormat.forPattern(Constants.FORMAT_DD_MM_YY_HH_MM)).toDateTimeISO()
    }

    override fun getBtnLocationOnClick() {

    }

    override fun createLocation(place: Place) {
        createdCustomLocation = if(event?.location != null && !event?.location!!.createdByUser){
            Location(event!!.location!!.id, place.latLng.longitude, place.latLng.latitude, place.name.toString(), place.address.toString(), false)
        }
        else
            Location(place.latLng.longitude, place.latLng.latitude,place.name.toString(),place.address.toString(), false)
    }

    override fun getMapUri() : String {
        return "geo:${event!!.location!!.latitude},${event!!.location!!.longitude}?q=" +
                "${event!!.location!!.latitude},${event!!.location!!.longitude}(${event!!.location!!.address})"
    }

    override fun setupLocationSpinner() {
        compositeDisposable!!.add(
                repository.database.locationDao().getUserCreatedLocations()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    locations ->

                    val locationsSpinner = mutableListOf<LocationSpinnerItem>()
                    locationsSpinner.add(LocationSpinnerItem(App.instance.getString(R.string.choose),null))
                    for(location: Location in locations){
                        locationsSpinner.add(LocationSpinnerItem(location.name,location.id))
                    }
                    locationsSpinner.add(LocationSpinnerItem(App.instance.getString(R.string.custom_location), null))
                    locationsSpinner.add(LocationSpinnerItem(App.instance.getString(R.string.new_location), null))
                    eventDetailsView.setupPlaceSpinnerAdapter(locationsSpinner)
                    when {
                        chosenLocationId != null -> {
                            for(locationSpinnerItem : LocationSpinnerItem in locationsSpinner)
                                if(locationSpinnerItem.locationId == chosenLocationId)
                                    eventDetailsView.locationSpinner!!.setSelection(locationsSpinner.indexOf(locationSpinnerItem))
                        }
                        event != null && event!!.location!=null && event!!.location!!.createdByUser -> {
                            for(locationSpinnerItem : LocationSpinnerItem in locationsSpinner)
                                if(locationSpinnerItem.locationId == event!!.location!!.id)
                                    eventDetailsView.locationSpinner!!.setSelection(locationsSpinner.indexOf(locationSpinnerItem))
                        }
                        event != null && event!!.location!=null && !event!!.location!!.createdByUser ->
                                eventDetailsView.locationSpinner!!.setSelection(locationsSpinner.size - 2)
                        else -> eventDetailsView.locationSpinner!!.setSelection(0)
                    }
                }
        )
    }

    override fun locationSpinnerOnItemsSelected(): AdapterView.OnItemSelectedListener {
        return object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when {
                    (parent!!.getItemAtPosition(position) as LocationSpinnerItem).name == App.instance.getString(R.string.custom_location) &&
                            ((event?.location == null || event?.location!!.createdByUser) && eventDetailsView.locationAddress.text.toString() == "") -> {
                        eventDetailsView.locationCreationLayout.visibility = View.VISIBLE
                        chosenLocationId = null
                        eventDetailsView.startPlacePicker()
                    }
                    //If Custom location is already selected
                    (parent.getItemAtPosition(position) as LocationSpinnerItem).name == App.instance.getString(R.string.custom_location) -> {
                        eventDetailsView.locationCreationLayout.visibility = View.VISIBLE
                        chosenLocationId = null
                    }
                    (parent.getItemAtPosition(position) as LocationSpinnerItem).name == App.instance.getString(R.string.new_location) -> {
                        eventDetailsView.startLocationDetailsActivity()
                    }
                    else -> {
                        chosenLocationId = (parent.getItemAtPosition(position) as LocationSpinnerItem).locationId
                        eventDetailsView.locationCreationLayout.visibility = View.GONE
                        createdCustomLocation = null
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    override fun onDestroy() {
        if(compositeDisposable!=null) {
            compositeDisposable!!.dispose()
            compositeDisposable = null
        }
    }

    private fun updateEvent() {
        val updatedEvent: Event
        when {
        //NoLocInteraction
            event!!.location == null && createdCustomLocation == null && chosenLocationId == null -> {
                updatedEvent = Event(event!!.id, getDateTimeFromEditTexts(), eventDetailsView.summary.text.toString(), remindTime!!)
                repository.database.eventDao().update(updatedEvent)
            }
        //UpdatingInUneditableWithLoc
            event!!.location != null && eventDetailsView.getUneditable() != null -> {
                updatedEvent = Event(event!!.id, getDateTimeFromEditTexts(), eventDetailsView.summary.text.toString(), remindTime!!, event!!.locationId!!)
                repository.database.eventDao().update(updatedEvent)
            }
        //AddCustLoc
            event!!.location == null && createdCustomLocation != null -> {
                updatedEvent = Event(event!!.id, getDateTimeFromEditTexts(), eventDetailsView.summary.text.toString(), remindTime!!, createdCustomLocation)
                repository.database.eventDao().updateEventWithAddedLocation(updatedEvent)
            }
        //AddUserLoc
            event!!.location == null && chosenLocationId != null -> {
                updatedEvent = Event(event!!.id, getDateTimeFromEditTexts(), eventDetailsView.summary.text.toString(), remindTime!!, chosenLocationId!!)
                repository.database.eventDao().updateEventWithAddedLocation(updatedEvent)
            }
        //UserLoc To CustomLoc
            event!!.location != null && event!!.location!!.createdByUser && createdCustomLocation != null -> {
                updatedEvent = Event(event!!.id, getDateTimeFromEditTexts(), eventDetailsView.summary.text.toString(), remindTime!!, createdCustomLocation)
                repository.database.eventDao().updateEventWithUserToCustomLoc(updatedEvent)
            }
        //CustomLoc To UserLoc
            event!!.location != null && !event!!.location!!.createdByUser && chosenLocationId != null -> {
                updatedEvent = Event(event!!.id, getDateTimeFromEditTexts(), eventDetailsView.summary.text.toString(), remindTime!!, event!!.location!!, chosenLocationId!!)
                repository.database.eventDao().updateEventWithCustomToUserLoc(updatedEvent)
            }
        //CustomLoc To CustomLoc
            event!!.location != null && !event!!.location!!.createdByUser && createdCustomLocation != null -> {
                updatedEvent = Event(event!!.id, getDateTimeFromEditTexts(), eventDetailsView.summary.text.toString(), remindTime!!, createdCustomLocation)
                repository.database.eventDao().updateEventWithCustomToCustomLoc(updatedEvent)
            }
        //UserLoc To UserLoc
            event!!.location !=null && event!!.location!!.createdByUser && chosenLocationId!=null -> {
                updatedEvent = Event(event!!.id, getDateTimeFromEditTexts(), eventDetailsView.summary.text.toString(), remindTime!!, chosenLocationId!!)
                repository.database.eventDao().update(updatedEvent)
            }
        //DeletionOfUserLoc
            event!!.location != null && event!!.location!!.createdByUser && createdCustomLocation == null && chosenLocationId == null -> {
                updatedEvent = Event(event!!.id, getDateTimeFromEditTexts(), eventDetailsView.summary.text.toString(), remindTime!!, event!!.location)
                repository.database.eventDao().updateEventWithUserLocDelete(updatedEvent)
            }
        //DeletionOfCustomLoc
            event!!.location != null && !event!!.location!!.createdByUser && createdCustomLocation == null && chosenLocationId == null -> {
                updatedEvent = Event(event!!.id, getDateTimeFromEditTexts(), eventDetailsView.summary.text.toString(), remindTime!!, event!!.location)
                repository.database.eventDao().updateEventWithCustLocDelete(updatedEvent)
            }
        }
    }

    private fun insertEvent(){
        event = when {
            chosenLocationId != null -> Event(getDateTimeFromEditTexts(), eventDetailsView.summary.text.toString(), remindTime!!, chosenLocationId!!)
            createdCustomLocation != null -> Event(getDateTimeFromEditTexts(), eventDetailsView.summary.text.toString(), remindTime!!, createdCustomLocation!!)
            else -> Event(getDateTimeFromEditTexts(), eventDetailsView.summary.text.toString(), remindTime!!)
        }
        eventId = repository.database.eventDao().insertEvent(event!!)
    }

    override fun onLocationDetailsResult(locationId: Long) {
        this.chosenLocationId = locationId
        setupLocationSpinner()
    }

}