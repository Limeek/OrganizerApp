package ru.limeek.organizer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.events_item.view.*
import ru.limeek.organizer.R
import ru.limeek.organizer.data.model.event.Event

class EventsListAdapter : RecyclerView.Adapter<EventsListAdapter.EventVH>(){
    var dataset: List<Event> = listOf()
    lateinit var onItemClick: (Event) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.events_item, parent, false)
        return EventVH(view)
    }

    override fun onBindViewHolder(holder: EventVH, position: Int) {
        holder.bind(dataset[position])
        holder.itemView.setOnClickListener { onItemClick.invoke(dataset[position]) }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    class EventVH(view: View): RecyclerView.ViewHolder(view){
        fun bind(event: Event){
            itemView.tvEventSummary.text = event.summary
            itemView.tvTime.text = event.getTime()
            itemView.tvLocation.text = getLocText(event)

        }

        private fun getLocText(event: Event): String{
            return if(event.location != null) {
                if (event.location!!.createdByUser)
                    event.location!!.name
                else
                    event.location!!.address
            }
            else ""
        }
    }
}