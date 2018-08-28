package ru.limeek.organizer.locations.presenter

import android.view.View
import ru.limeek.organizer.locations.view.LocationView

class LocationPresenterImpl (val locationView: LocationView) : LocationPresenter {
    override fun fabOnClick() : View.OnClickListener{
        return View.OnClickListener {locationView.startDetailsActivity()}
    }
}