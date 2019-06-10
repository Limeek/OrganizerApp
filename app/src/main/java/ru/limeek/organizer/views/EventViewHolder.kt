package ru.limeek.organizer.views

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.events_item.view.*
import ru.limeek.organizer.presenters.EventsViewHolderPresenter
import ru.limeek.organizer.data.model.event.Event

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