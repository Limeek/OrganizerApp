package ru.limeek.organizer.views


import android.content.Context
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.data.model.location.LocationSpinnerItem

interface EventDetailsView : View {

    fun getEvent() : Event?
    fun getEventId() : Long?
    fun getContext() : Context
    fun getUneditable() : Boolean?
    fun setupNotificationSpinnerAdapter()
    fun showErrorNotificationAndHideLayout()
    fun setupPlaceSpinnerAdapter(items: List<LocationSpinnerItem>)
    fun startLocationDetailsActivity()
    fun startPlacePicker()
    fun startCalendarActivity()

    fun updateDate(date: String)
    fun updateTime(time: String)
    fun updateSummary(summary: String)
    fun updateNotification(isEnabled: Boolean)
    fun updateLocationChooseVisibility(value: Boolean)
    fun updateLocationCreationVisibility(value: Boolean)
    fun updateLocationSwitch(value: Boolean)
    fun updateLocationAddress(address: String)

    fun getTime(): String
    fun getDate(): String
    fun isNotificationChecked(): Boolean
    fun getLocation(): String
    fun getSummary(): String
    fun isLocationChecked(): Boolean

    fun setRemindSpinnerSelection(position: Int)
    fun setLocationSpinnerSelecetion(position: Int)

    fun notifyNotificationAdapter()
    fun notifyLocationAdapter()

}