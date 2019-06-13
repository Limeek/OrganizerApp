package ru.limeek.organizer.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.limeek.organizer.data.model.location.Location
import ru.limeek.organizer.databinding.LocationItemBinding

class LocationsAdapter : RecyclerView.Adapter<LocationsAdapter.LocationVH>() {

    var dataset: List<Location> = listOf()
    lateinit var onItemClick: (Location) -> Unit

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: LocationVH, position: Int) {
        holder.binding.location = dataset[position]
        holder.binding.root.setOnClickListener { onItemClick.invoke(dataset[position]) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationVH {
        val binding = LocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationVH(binding)
    }

    class LocationVH(var binding: LocationItemBinding): RecyclerView.ViewHolder(binding.root)
}