package ru.limeek.organizer.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.limeek.organizer.data.model.event.Event
import ru.limeek.organizer.databinding.EventsItemBinding

class EventsListAdapter : RecyclerView.Adapter<EventsListAdapter.EventVH>(){
    var dataset: List<Event> = listOf()
    lateinit var onItemClick: (Event) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventVH{
        val binding = EventsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventVH(binding)
    }

    override fun onBindViewHolder(holder: EventVH, position: Int) {
        holder.binding.event = dataset[position]
        holder.binding.locationString = if(dataset[position].location != null) {
            if(dataset[position].location!!.createdByUser)
                dataset[position].location!!.name
            else
                dataset[position].location!!.address
        }
        else {
            ""
        }
        holder.binding.root.setOnClickListener { onItemClick.invoke(holder.binding.event!!) }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    class EventVH(var binding: EventsItemBinding): RecyclerView.ViewHolder(binding.root)
}