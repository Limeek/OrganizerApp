package ru.limeek.organizer.views

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.limeek.organizer.R
import ru.limeek.organizer.presenters.LocationsAdapterPresenter

class LocationsAdapter(var context: Context) : RecyclerView.Adapter<LocationViewHolder>() {
    var presenter : LocationsAdapterPresenter = LocationsAdapterPresenter(this)

    override fun getItemCount(): Int {
        return presenter.getCount()
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.presenter.locationsAdapterPresenter = presenter
        holder.presenter.position = position
        holder.presenter.bind()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder(LayoutInflater.from(context).inflate(R.layout.location_item, parent, false))
    }

    fun onDestroy(){
        presenter.onDestroy()
    }
}