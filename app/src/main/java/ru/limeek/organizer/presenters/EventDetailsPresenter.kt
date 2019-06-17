package ru.limeek.organizer.presenters

import android.widget.AdapterView
import org.joda.time.DateTime
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.model.event.RemindTime

interface EventDetailsPresenter {
    var remindTimeList : MutableList<RemindTime>?
    var event : Event?

    fun updateUI()
    fun submit()
    fun delete()
    fun onCreate()
    suspend fun sendToAlarmManager()
    fun notificationSpinnerOnItemSelected() : AdapterView.OnItemSelectedListener
    fun locationSpinnerOnItemsSelected() : AdapterView.OnItemSelectedListener
    fun updateSpinnerItems()
    fun getDateTimeFromEditTexts() : DateTime
    fun getBtnLocationOnClick()
//    fun createLocation(place: Place)
    fun getMapUri() : String
    fun setupLocationSpinner()
    fun onDestroy()
    fun onLocationDetailsResult(locationId: Long)
}