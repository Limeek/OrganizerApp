package ru.limeek.organizer.views

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.limeek.organizer.R
import ru.limeek.organizer.presenters.EventsAdapterPresenter

class EventsListAdapter(var context : Context?) : RecyclerView.Adapter<EventViewHolder>(){

    var presenter : EventsAdapterPresenter = EventsAdapterPresenter(this)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(LayoutInflater.from(context).inflate(R.layout.events_item, parent, false))
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.presenter.eventsAdapterPresenter = presenter
        holder.presenter.position = position
        holder.presenter.bind()
    }

    override fun getItemCount(): Int {
        return presenter.getCount()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun refreshRecycler(){
        presenter.updateEvents()
    }

    fun onDestroy(){
        presenter.onDestroy()
        context = null
    }
}