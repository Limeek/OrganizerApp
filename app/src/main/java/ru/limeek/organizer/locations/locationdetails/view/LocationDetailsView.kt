package ru.limeek.organizer.locations.locationdetails.view

import android.os.Bundle
import android.widget.EditText
import ru.limeek.organizer.model.location.Location
import ru.limeek.organizer.mvp.View

interface LocationDetailsView : View {
    fun getLocation() : Location?
    fun getFromEventDetails() : Boolean?
    fun startEventDetailsWithResult(bundle: Bundle)
    fun startLocationActivity()

    fun updateName(name: String)
    fun updateAddress(address: String)

    fun getName(): String
    fun getAddress(): String
}