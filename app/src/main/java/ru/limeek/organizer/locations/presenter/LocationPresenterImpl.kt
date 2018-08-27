package ru.limeek.organizer.locations.presenter

import android.view.View
import ru.limeek.organizer.locations.view.LocationView
import javax.inject.Inject

class LocationPresenterImpl (val locationView: LocationView) : LocationPresenter {
    override fun fabOnClick() : View.OnClickListener{
        return View.OnClickListener {locationView.startDetailsActivity()}
    }
}