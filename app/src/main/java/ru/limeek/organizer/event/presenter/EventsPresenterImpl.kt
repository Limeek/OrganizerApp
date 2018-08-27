package ru.limeek.organizer.event.presenter

import android.view.View
import ru.limeek.organizer.event.view.EventFragmentView

class EventsPresenterImpl(var eventFragmentView: EventFragmentView?) : EventsPresenter {
    val logTag = "EventsPresenter"

    override fun onCreate(){
    }

    override fun onFabClick() : View.OnClickListener {
        return View.OnClickListener {
           eventFragmentView!!.startDetailsActivity()
        }
    }

    override fun onDestroy() {
        eventFragmentView = null
    }
}
