package ru.limeek.organizer.event.eventdetails.view


import android.content.Context
import android.support.v7.widget.SwitchCompat
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import ru.limeek.organizer.model.Event.Event
import ru.limeek.organizer.model.Event.RemindTime
import ru.limeek.organizer.model.Location.LocationSpinnerItem
import ru.limeek.organizer.mvp.View

interface EventDetailsView : View {
    var date : EditText
    var time : EditText
    var summary : EditText
    var notification : SwitchCompat
    var notificationAdapter : ArrayAdapter<RemindTime>
    var notificationLayout : LinearLayout
    var remind : Spinner
    var locationSwitch : SwitchCompat?
    var locationCreationLayout : LinearLayout
    var locationAddress : EditText
    var locationSpinner : Spinner?
    var locationAdapter : ArrayAdapter<LocationSpinnerItem>
    var locationChooseLayout: LinearLayout


    fun getEvent() : Event?
    fun getEventId() : Long?
    fun getContext() : Context
    fun getUneditable() : Boolean?
    fun setupNotificationSpinnerAdapter()
    fun showErrorNotificationAndHideLayout()
    fun setupPlaceSpinnerAdapter(items: List<LocationSpinnerItem>)
    fun startLocationDetailsActivity()
    fun startPlacePicker()
}