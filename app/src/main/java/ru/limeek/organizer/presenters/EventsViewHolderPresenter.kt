package ru.limeek.organizer.presenters

import android.os.Bundle
import android.view.View
import ru.limeek.organizer.views.EventViewHolder

class EventsViewHolderPresenter(var view : EventViewHolder){
    val logTag = "EventsViewHolderPresenter"
    var position : Int = 0

    lateinit var eventsAdapterPresenter: EventsAdapterPresenter

    fun bind(){
        view.eventSummary.text = eventsAdapterPresenter.events[position].summary
        view.eventTime.text = eventsAdapterPresenter.events[position].getTime()
        if(eventsAdapterPresenter.events[position].location != null) {
            if(eventsAdapterPresenter.events[position].location!!.createdByUser)
                view.eventPlace.text = eventsAdapterPresenter.events[position].location!!.name
            else
                view.eventPlace.text = eventsAdapterPresenter.events[position].location!!.address
        }
        else {
            view.eventPlace.visibility = View.GONE
            view.imageLocation.visibility = View.GONE
        }
    }


    fun onClick(){
        val bundle = Bundle()
        bundle.putParcelable("event", eventsAdapterPresenter.getItemAt(position))
        bundle.putBoolean("uneditable", true)
        view.startEventDetailsActivity(bundle)
    }
}