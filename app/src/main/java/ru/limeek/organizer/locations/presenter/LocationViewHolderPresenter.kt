package ru.limeek.organizer.locations.presenter

import android.util.Log
import ru.limeek.organizer.locations.view.LocationViewHolder

class LocationViewHolderPresenter(var locationViewHolder: LocationViewHolder) {

    val logTag = "LocationViewHolderPresenter"
    var position : Int = 0

    lateinit var locationsAdapterPresenter: LocationsAdapterPresenter

    fun bind(){
        locationViewHolder.locationAddress.text = locationsAdapterPresenter.locations[position].address
        locationViewHolder.locationName.text = locationsAdapterPresenter.locations[position].name
        Log.wtf(logTag, locationViewHolder.locationAddress.text.toString())
        Log.wtf(logTag, locationViewHolder.locationName.text.toString())
    }

    fun onClick(){
        locationViewHolder.startLocationDetailsActivity(locationsAdapterPresenter.getItemAt(position))
    }
}