package ru.limeek.organizer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.location_item.view.*
import ru.limeek.organizer.R
import ru.limeek.organizer.data.model.location.Location

class LocationsAdapter : RecyclerView.Adapter<LocationsAdapter.LocationVH>() {

    var dataset: List<Location> = listOf()
    lateinit var onItemClick: (Location) -> Unit

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: LocationVH, position: Int) {
        holder.bind(dataset[position])
        holder.itemView.setOnClickListener { onItemClick.invoke(dataset[position]) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationVH {
        return LocationVH(LayoutInflater.from(parent.context).inflate(R.layout.location_item, parent, false))
    }

    class LocationVH(view: View): RecyclerView.ViewHolder(view){
        fun bind(location: Location){
            itemView.tvLocationName.text = location.name
            itemView.tvLocationAddress.text = location.address
        }
    }
}