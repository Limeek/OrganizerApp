package ru.limeek.organizer.event.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.events_item.view.*
import ru.limeek.organizer.event.eventdetails.view.EventDetailsActivity
import ru.limeek.organizer.event.presenter.EventsViewHolderPresenter
import ru.limeek.organizer.model.Event.Event

class EventViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    var presenter : EventsViewHolderPresenter = EventsViewHolderPresenter(this)

    init{
        view.setOnClickListener{presenter.onClick()}
    }

    var eventTime = view.tvTime
    var eventSummary = view.tvEventSummary
    var eventPlace = view.tvLocation
    var imageLocation = view.ivLocation

    fun startEventDetailsActivity(bundle: Bundle){
        val intent = Intent(itemView.context, EventDetailsActivity::class.java)
        intent.putExtra("event", bundle.getParcelable<Event>("event"))
        intent.putExtra("uneditable", bundle.getBoolean("uneditable"))
        itemView.context.startActivity(intent)
    }
}