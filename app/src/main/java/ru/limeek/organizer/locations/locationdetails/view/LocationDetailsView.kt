package ru.limeek.organizer.locations.locationdetails.view

import android.os.Bundle
import android.widget.EditText
import ru.limeek.organizer.model.Location.Location
import ru.limeek.organizer.mvp.View

interface LocationDetailsView : View {
    var name : EditText
    var address : EditText

    fun getLocation() : Location?
    fun getFromEventDetails() : Boolean?
    fun startEventDetailsWithResult(bundle: Bundle)
    fun startLocationActivity()
}