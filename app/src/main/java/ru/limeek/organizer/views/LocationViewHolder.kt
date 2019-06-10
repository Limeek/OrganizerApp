package ru.limeek.organizer.views

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.location_item.view.*
import ru.limeek.organizer.presenters.LocationViewHolderPresenter
import ru.limeek.organizer.data.model.location.Location

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