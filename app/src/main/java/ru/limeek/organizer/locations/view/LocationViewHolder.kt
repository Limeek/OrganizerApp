package ru.limeek.organizer.locations.view

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.location_item.view.*
import ru.limeek.organizer.locations.locationdetails.view.LocationDetailsActivity
import ru.limeek.organizer.locations.presenter.LocationViewHolderPresenter
import ru.limeek.organizer.model.Location.Location

class LocationViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
    var presenter = LocationViewHolderPresenter(this)

    init{
        view.setOnClickListener{presenter.onClick()}
    }

    var locationName = view.tvLocationName
    var locationAddress = view.tvLocationAddress

    fun startLocationDetailsActivity(location: Location){
        val intent = Intent(view.context, LocationDetailsActivity::class.java)
        intent.putExtra("location", location)
        view.context.startActivity(intent)
    }
}