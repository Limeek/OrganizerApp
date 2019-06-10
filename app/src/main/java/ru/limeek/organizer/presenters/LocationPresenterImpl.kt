package ru.limeek.organizer.presenters

import android.view.View
import ru.limeek.organizer.views.LocationView

class LocationPresenterImpl (val locationView: LocationView) : LocationPresenter {
    override fun fabOnClick() : View.OnClickListener{
        return View.OnClickListener {locationView.startDetailsActivity()}
    }
}