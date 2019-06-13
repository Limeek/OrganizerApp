package ru.limeek.organizer.views

import android.os.Bundle
import ru.limeek.organizer.data.model.location.Location

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