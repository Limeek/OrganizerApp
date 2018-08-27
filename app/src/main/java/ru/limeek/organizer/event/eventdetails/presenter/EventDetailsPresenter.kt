package ru.limeek.organizer.event.eventdetails.presenter

import android.widget.AdapterView
import com.google.android.gms.location.places.Place
import io.reactivex.Observable
import org.joda.time.DateTime
import ru.limeek.organizer.event.eventdetails.view.DatePickerDialogFragment
import ru.limeek.organizer.event.eventdetails.view.TimePickerDialogFragment
import ru.limeek.organizer.model.Event.Event
import ru.limeek.organizer.model.Event.RemindTime

interface EventDetailsPresenter {
    var remindTimeList : MutableList<RemindTime>?
    var event : Event?

    fun updateUI()
    fun submit()
    fun delete()
    fun onCreate()
    fun createDateDialog() : DatePickerDialogFragment
    fun createTimeDialog() : TimePickerDialogFragment
    fun sendToAlarmManager() : Observable<Unit>
    fun notificationSpinnerOnItemSelected() : AdapterView.OnItemSelectedListener
    fun locationSpinnerOnItemsSelected() : AdapterView.OnItemSelectedListener
    fun updateSpinnerItems()
    fun getDateTimeFromEditTexts() : DateTime
    fun getBtnLocationOnClick()
    fun createLocation(place: Place)
    fun getMapUri() : String
    fun setupLocationSpinner()
    fun onDestroy()
    fun onLocationDetailsResult(locationId: Long)
}